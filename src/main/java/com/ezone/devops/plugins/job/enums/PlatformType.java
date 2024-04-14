package com.ezone.devops.plugins.job.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum PlatformType {

    AIX("aix"),
    ANDROID("android"),
    DARWIN("darwin"),
    DRAGONFLY("dragonfly"),
    FREEBSD("freebsd"),
    HURD("hurd"),
    ILLUMOS("illumos"),
    IOS("ios"),
    JS("js"),
    LINUX("linux"),
    NACL("nacl"),
    NETBSD("netbsd"),
    OPENBSD("openbsd"),
    PLAN9("plan9"),
    SOLARIS("solaris"),
    WINDOWS("windows"),
    ZOS("zos");

    @Getter
    private String type;

}
