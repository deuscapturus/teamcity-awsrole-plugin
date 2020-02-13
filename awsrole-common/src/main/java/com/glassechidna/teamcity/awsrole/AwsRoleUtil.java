package com.glassechidna.teamcity.awsrole;

import software.amazon.awssdk.services.sts.model.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AwsRoleUtil {
    public static String getRoleArn(Map<String, String> params) {
        String roleArn = params.getOrDefault(AwsRoleConstants.ROLE_ARN_PARAMETER, "");
        if (roleArn == "") {
            // fall back to checking legacy parameter name
            roleArn = params.getOrDefault(AwsRoleConstants.LEGACY_ROLE_ARN_PARAMETER, "");
            if (roleArn == "") {
                return "";
            }
        }
        return roleArn;
    }

    public static String getExternalId(Map<String, String> params) {
        return params.getOrDefault(AwsRoleConstants.EXTERNAL_ID_PARAMETER, "");
    }

    public static String getSessionName(Map<String, String> params) {
        String sessionName = params.getOrDefault(AwsRoleConstants.SESSION_NAME_PARAMETER, "");

        if (sessionName.length() > AwsRoleConstants.MAXIMUM_ROLE_SESSION_NAME_LENGTH) {
            sessionName = sessionName.substring(sessionName.length() - AwsRoleConstants.MAXIMUM_ROLE_SESSION_NAME_LENGTH);
        }
        return sessionName;
    }

    public static List<Tag> getSessionTags(Map<String, String> params) {
        String rawTags = params.getOrDefault(AwsRoleConstants.SESSION_TAGS_PARAMETER, "");
        List<Tag> tags = new ArrayList<>();
        String[] lines = rawTags.split(System.getProperty("line.separator"));
        for(String tmpLine : lines){
            String[] parts = tmpLine.split("=", 2);
            // Only process correctly formatted tags. This should log somewhere
            if (parts.length == 2) {
                tags.add(Tag.builder().key(parts[0]).value(parts[1]).build());
            }
        }
        return tags;
    }
}