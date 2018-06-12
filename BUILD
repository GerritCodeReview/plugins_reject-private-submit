load(
    "//tools/bzl:plugin.bzl",
    "gerrit_plugin",
)

gerrit_plugin(
  name = 'reject-private-submit',
  srcs = glob(['src/main/java/**/*.java']),
  resources = glob(['src/main/resources/**/*']),
  manifest_entries = [
    'Gerrit-PluginName: reject-private-submit',
    'Gerrit-Module: com.googlesource.gerrit.plugins.rejectprivate.RejectPrivateSubmitListener$Module'
  ],
)

