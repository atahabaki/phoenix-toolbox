package dev.atahabaki.phoenixtoolbox

fun deleteCache(packageName: String) {
    execRoot("rm -rf /cache/*", "${packageName}.clearCache")
    execRoot("rm -rf /data/user/0/*/cache", "${packageName}.clearCache")
}