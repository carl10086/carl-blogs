package com.cb.it.rules


data class MachineAuditFacts(
    val contents: List<String>,
    val urls: List<String>,
    val version: Int,
    var neteaseImageAudit: Any,
    var neteaseContentAudit: Any,
    var shumeiImageAudit: Any,
)
