{
  pkgs ? import <nixpkgs> { 
    config.android_sdk.accept_license = true;
    config.allowUnfree = true;
    overlays = [
      (self: super: {
        androidPkgs_8_0 = super.androidenv.composeAndroidPackages {
          platformVersions = [ "36" ];
          abiVersions = [ "x86" "x86_64"];
        };
      })
    ];
  }
}:

(pkgs.buildFHSEnv {
  name = "android-sdk-env";
  targetPkgs = pkgs: (with pkgs;
    [
      androidPkgs_8_0.androidsdk
      glibc
    ]);
  runScript = "bash";
}).env

#vim:set sw=2 ts=2 et:
