package com.ezone.devops.pipeline.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.StringWriter;
import java.util.UUID;

@Slf4j
public class FreemarkerUtil {

    public static String parseExpression(String expressionContent, Object dataModel) {
        if (StringUtils.isBlank(expressionContent) || dataModel == null) {
            return null;
        }

        StringWriter output = new StringWriter();
        Configuration configuration = new Configuration();

        try {
            configuration.setSetting("classic_compatible", "true");
            String name = UUID.randomUUID().toString();
            Template template = new Template(name, expressionContent, configuration);
            template.process(dataModel, output);
        } catch (Exception e) {
            log.error("parse free marker error ", e);
            return null;
        }
        return output.toString();
    }
}
