package com.ezone.devops.pipeline.service.impl;

import com.ezone.devops.pipeline.dao.PipelinePermissionDao;
import com.ezone.devops.pipeline.enums.PipelinePermissionType;
import com.ezone.devops.pipeline.enums.RepoPermissionType;
import com.ezone.devops.pipeline.model.Pipeline;
import com.ezone.devops.pipeline.model.PipelinePermission;
import com.ezone.devops.pipeline.service.PipelinePermissionService;
import com.ezone.devops.pipeline.service.PipelineService;
import com.ezone.devops.pipeline.service.RepoService;
import com.ezone.devops.pipeline.vo.RepoVo;
import com.ezone.devops.pipeline.vo.UserInfo;
import com.ezone.devops.pipeline.web.request.PipelinePermissionPayload;
import com.ezone.ezbase.iam.service.IAMCenterService;
import com.ezone.galaxy.framework.common.bean.BaseException;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Service
public class PipelinePermissionServiceImpl implements PipelinePermissionService {

    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private PipelinePermissionDao pipelinePermissionDao;
    @Autowired
    private IAMCenterService iamCenterService;
    @Autowired
    private RepoService repoService;

    @Override
    public PipelinePermissionPayload getDetails(Pipeline pipeline, String username) {
        List<PipelinePermission> pipelinePermissions = pipelinePermissionDao.findByPipelineId(pipeline.getId());
        if (CollectionUtils.isEmpty(pipelinePermissions)) {
            return null;
        }

        Set<UserInfo> admins = Sets.newHashSet();
        Set<UserInfo> maintainers = Sets.newHashSet();
        Set<UserInfo> operators = Sets.newHashSet();
        Set<UserInfo> readers = Sets.newHashSet();
        for (PipelinePermission pipelinePermission : pipelinePermissions) {
            UserInfo userInfo = new UserInfo();
            userInfo.setName(pipelinePermission.getName());
            userInfo.setType(pipelinePermission.getType());
            switch (pipelinePermission.getPipelinePermissionType()) {
                case PIPELINE_ADMIN:
                    admins.add(userInfo);
                    continue;
                case PIPELINE_MAINTAINER:
                    maintainers.add(userInfo);
                    continue;
                case PIPELINE_OPERATOR:
                    operators.add(userInfo);
                    continue;
                case PIPELINE_READER:
                    readers.add(userInfo);
                    continue;
                default:
                    readers.add(userInfo);
            }
        }

        boolean allowEdit = false;
        RepoVo repoVo = repoService.getByRepoKey(pipeline.getCompanyId(), pipeline.getRepoKey());
        boolean repoAdmin = repoService.isRepoAdmin(repoVo, username);
        if (repoAdmin) {
            allowEdit = true;
        } else {
            boolean managePermission = hasPermission(repoVo, pipeline, PipelinePermissionType.PIPELINE_ADMIN, username);
            allowEdit = managePermission;
        }

        return new PipelinePermissionPayload(pipeline.isInheritRepoPermission(), allowEdit, admins, maintainers, operators, readers);
    }

    @Override
    public TreeSet<Long> getAuthorizedPipeline(RepoVo repoVo, String username) {
        String repoKey = repoVo.getRepoKey();
        List<String> groupNames = iamCenterService.queryGroupNamesByCompanyUsername(repoVo.getCompanyId(), username);

        List<PipelinePermission> userPermissions = pipelinePermissionDao.findByRepoKeyAndUsername(repoKey, username);
        List<PipelinePermission> groupPermissions = pipelinePermissionDao.findByRepoKeyAndGroups(repoKey, groupNames);

        TreeSet<Long> pipelineIds = Sets.newTreeSet();
        if (CollectionUtils.isNotEmpty(userPermissions)) {
            for (PipelinePermission userPermission : userPermissions) {
                pipelineIds.add(userPermission.getPipelineId());
            }
        }

        if (CollectionUtils.isNotEmpty(groupPermissions)) {
            for (PipelinePermission groupPermission : groupPermissions) {
                pipelineIds.add(groupPermission.getPipelineId());
            }
        }

        return pipelineIds;
    }


    private List<PipelinePermission> assemble(Pipeline pipeline, PipelinePermissionType pipelinePermissionType, Set<UserInfo> userInfos) {
        List<PipelinePermission> pipelinePermissions = Lists.newArrayList();
        for (UserInfo userInfo : userInfos) {
            PipelinePermission pipelinePermission = new PipelinePermission();
            pipelinePermission.setCompanyId(pipeline.getCompanyId());
            pipelinePermission.setRepoKey(pipeline.getRepoKey());
            pipelinePermission.setPipelineId(pipeline.getId());
            pipelinePermission.setPipelinePermissionType(pipelinePermissionType);
            pipelinePermission.setName(userInfo.getName());
            pipelinePermission.setType(userInfo.getType());
            pipelinePermissions.add(pipelinePermission);
        }


        return pipelinePermissions;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updatePermission(Pipeline pipeline, PipelinePermissionPayload payload) {
        pipelinePermissionDao.deleteByPipelineId(pipeline.getId());

        if (payload.isInheritRepoPermission()) {
            pipeline.setInheritRepoPermission(true);
            pipelineService.updatePipeline(pipeline);
            return true;
        }

        List<PipelinePermission> all = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(payload.getAdmins())) {
            List<PipelinePermission> admins = assemble(pipeline, PipelinePermissionType.PIPELINE_ADMIN, payload.getAdmins());
            all.addAll(admins);
        }

        if (CollectionUtils.isNotEmpty(payload.getMaintainers())) {
            List<PipelinePermission> maintainers = assemble(pipeline, PipelinePermissionType.PIPELINE_MAINTAINER, payload.getMaintainers());
            all.addAll(maintainers);
        }

        if (CollectionUtils.isNotEmpty(payload.getOperators())) {
            List<PipelinePermission> operators = assemble(pipeline, PipelinePermissionType.PIPELINE_OPERATOR, payload.getOperators());
            all.addAll(operators);
        }

        if (CollectionUtils.isNotEmpty(payload.getReaders())) {
            List<PipelinePermission> readers = assemble(pipeline, PipelinePermissionType.PIPELINE_READER, payload.getReaders());
            all.addAll(readers);
        }

        if (CollectionUtils.isEmpty(all)) {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "人员权限设置不能全为空");
        }

        pipeline.setInheritRepoPermission(false);
        pipelineService.updatePipeline(pipeline);

        pipelinePermissionDao.save(all);
        return true;
    }

    @Override
    public boolean hasPermission(RepoVo repoVo, Pipeline pipeline, PipelinePermissionType pipelinePermissionType, String username) {
        if (pipeline.isInheritRepoPermission()) {
            RepoPermissionType repoPermissionType = transfer(pipelinePermissionType);
            if (repoPermissionType == null) {
                return false;
            }
            return repoService.hasPermission(repoVo, username, repoPermissionType);
        } else {
            boolean repoAdmin = repoService.isRepoAdmin(repoVo, username);
            if (repoAdmin) {
                return true;
            }

            Set<PipelinePermissionType> accessPermissions = Sets.newHashSet();
            switch (pipelinePermissionType) {
                case PIPELINE_ADMIN:
                    accessPermissions.add(PipelinePermissionType.PIPELINE_ADMIN);
                    break;
                case PIPELINE_MAINTAINER:
                    accessPermissions.add(PipelinePermissionType.PIPELINE_ADMIN);
                    accessPermissions.add(PipelinePermissionType.PIPELINE_MAINTAINER);
                    break;
                case PIPELINE_OPERATOR:
                    accessPermissions.add(PipelinePermissionType.PIPELINE_ADMIN);
                    accessPermissions.add(PipelinePermissionType.PIPELINE_MAINTAINER);
                    accessPermissions.add(PipelinePermissionType.PIPELINE_OPERATOR);
                    break;
                default:
                    accessPermissions.add(PipelinePermissionType.PIPELINE_ADMIN);
                    accessPermissions.add(PipelinePermissionType.PIPELINE_MAINTAINER);
                    accessPermissions.add(PipelinePermissionType.PIPELINE_OPERATOR);
                    accessPermissions.add(PipelinePermissionType.PIPELINE_READER);
            }

            List<String> groupNames = iamCenterService.queryGroupNamesByCompanyUsername(pipeline.getCompanyId(), username);

            String repoKey = repoVo.getRepoKey();
            Long pipelineId = pipeline.getId();
            List<PipelinePermission> userPermissions = pipelinePermissionDao.findByRepoKeyAndPipelineIdAndUsername(repoKey, pipelineId, username);
            List<PipelinePermission> groupPermissions = pipelinePermissionDao.findByRepoKeyAndPipelineIdAndGroups(repoKey, pipelineId, groupNames);


            Set<PipelinePermissionType> allPermissions = Sets.newHashSet();
            if (CollectionUtils.isNotEmpty(userPermissions)) {
                for (PipelinePermission pipelinePermission : userPermissions) {
                    allPermissions.add(pipelinePermission.getPipelinePermissionType());
                }
            }

            if (CollectionUtils.isNotEmpty(groupPermissions)) {
                for (PipelinePermission pipelinePermission : groupPermissions) {
                    allPermissions.add(pipelinePermission.getPipelinePermissionType());
                }
            }

            return CollectionUtils.containsAny(allPermissions, accessPermissions);
        }
    }

    private RepoPermissionType transfer(PipelinePermissionType pipelinePermissionType) {
        switch (pipelinePermissionType) {
            case PIPELINE_ADMIN:
                return RepoPermissionType.PIPELINE_MANAGE;
            case PIPELINE_MAINTAINER:
                return RepoPermissionType.PIPELINE_UPDATE;
            case PIPELINE_OPERATOR:
                return RepoPermissionType.PIPELINE_EXECUTE;
            case PIPELINE_READER:
                return RepoPermissionType.PIPELINE_VIEW;
        }

        return null;
    }
}