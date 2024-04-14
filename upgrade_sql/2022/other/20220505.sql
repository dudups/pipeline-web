# 增加.net的6.0 7.0版本
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('DOTNET-COMPILE', 'dotnet6.0', 'ezone-public/mcr.microsoft.com/dotnet/sdk', '6.0');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('DOTNET-COMPILE', 'dotnet7.0', 'ezone-public/mcr.microsoft.com/dotnet/sdk', '7.0');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('DOTNET-COMPILE-DOCKER', 'dotnet6.0', 'ezone-public/mcr.microsoft.com/dotnet/sdk', '6.0');
INSERT INTO `build_image`(`job_type`, `display_name`, `image`, `tag`) VALUES ('DOTNET-COMPILE-DOCKER', 'dotnet7.0', 'ezone-public/mcr.microsoft.com/dotnet/sdk', '7.0');
