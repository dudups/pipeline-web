package com.ezone.devops.plugins.job.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ArchType {

    I386("386"),
    AMD64("amd64"),
    AMD64P32("amd64p32"),
    ARM("arm"),
    ARM64("arm64"),
    ARM64BE("arm64be"),
    ARMBE("armbe"),
    LOONG64("loong64"),
    MIPS("mips"),
    MIPS64("mips64"),
    MIPS64LE("mips64le"),
    MIPS64P32("mips64p32"),
    MIPS64P32LE("mips64p32le"),
    MIPSLE("mipsle"),
    PPC("ppc"),
    PPC64("ppc64"),
    PPC64LE("ppc64le"),
    RISCV("riscv"),
    RISCV64("riscv64"),
    S390("s390"),
    S390X("s390x"),
    SPARC("sparc"),
    SPARC64("sparc64"),
    WASM("wasm");

    @Getter
    private String type;

}
