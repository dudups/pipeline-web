package com.ezone.devops.pipeline.rocketmq.producer;

import com.ezone.ezbase.iam.bean.enums.BillingItem;
import com.ezone.ezbase.iam.bean.enums.BillingMsgUnit;
import com.ezone.ezbase.iam.bean.enums.SystemType;
import com.ezone.ezbase.iam.bean.mq.CompanyConsumptionMsg;
import com.ezone.galaxy.framework.mq.annotation.producer.CommonMessage;
import com.ezone.galaxy.framework.mq.annotation.producer.RocketMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@RocketMessage(groupName = "ezpipeline")
public class ResourceChargeProducer {

    @CommonMessage(topic = "COMPANY_CONSUMPTION", tag = "UPDATE")
    public CompanyConsumptionMsg sendCharge(Long companyId, Date whenStart, Date whenFinished, BillingItem item) {
        CompanyConsumptionMsg companyConsumptionMsg = new CompanyConsumptionMsg();
        companyConsumptionMsg.setSystemType(SystemType.EZPIPELINE);
        companyConsumptionMsg.setTimeStamp(System.currentTimeMillis());
        companyConsumptionMsg.setCompanyId(companyId);
        companyConsumptionMsg.setUnit(BillingMsgUnit.MILLI_SECOND);
        companyConsumptionMsg.setItem(item);
        companyConsumptionMsg.setValue(whenFinished.getTime() - whenStart.getTime());
        log.info("send charge:[{}]", companyConsumptionMsg);
        return companyConsumptionMsg;
    }

    @CommonMessage(topic = "COMPANY_CONSUMPTION", tag = "UPDATE")
    public CompanyConsumptionMsg sendStorageCharge(Long companyId, long value) {
        CompanyConsumptionMsg companyConsumptionMsg = new CompanyConsumptionMsg();
        companyConsumptionMsg.setSystemType(SystemType.EZPIPELINE);
        companyConsumptionMsg.setTimeStamp(System.currentTimeMillis());
        companyConsumptionMsg.setCompanyId(companyId);
        companyConsumptionMsg.setUnit(BillingMsgUnit.BYTE);
        companyConsumptionMsg.setItem(BillingItem.STORAGE_CAPACITY);
        companyConsumptionMsg.setValue(value);
        log.info("send charge:[{}]", companyConsumptionMsg);
        return companyConsumptionMsg;
    }

}
