# OmegaT customization

For historical reasons, OmegaT is very opinionated in the factory settings it offers as regards user interface and some behaviour. Tweaking the default preferences by hand one by one is tedious and error-prone, especially for large teams where preferences are expected to be homogeneous across the team. This automated customization removes the burden of doing that manually from translators working in a team.

## What is customization

On the one hand, OmegaT provides certain preferences, where the option selected by default is not the most convenient one (as regard modern CAT tool best practices). On the other hand, OmegaT is very extensible by means of scritps and plugins. 

The customization consists of tweaked preferences and configuration files as well as script and plugins.

## Installing and customizing OmegaT

### Windows

Please follow [this guide](https://slides.com/capstan/omegat5-installation-and-customization-guide). 

Alternatively, these are the basic steps (a summary of the guide above):

1. Make sure file extensions are displayed in your computer, you can follow tips [here](https://www.howtogeek.com/205086/beginner-how-to-make-windows-show-file-extensions/).
2. Install OmegaT 4.3.3, you can find it [here](https://sourceforge.net/projects/omegat/files/OmegaT%20-%20Standard/OmegaT%204.3.3/OmegaT_4.3.3_Windows_64_Signed.exe/download).
3. Download the customization script from [here](https://cat.capstan.be/OmegaT/installer/scripts/updateConfigBundle.groovy). When the page shows the script, you can do **Ctrl+S** (or right click and choose Save). Please make sure your browser doesn't add a `.txt` extension or any other (the file extension should be `.groovy`).
4. In OmegaT, go to **Tools** > **Scripting** > **File** > **Open Script** and open the customization script from the location where your browser saved it (probably in your Downloads folder).
5. When the script is open in the scripting window, press the **Run** button.

The script will run and customize your installation. You might need to restart OmegaT at the end.

<!-- @TODO: write instructions for chocolatey in PowerShell -->

### Linux (both desktop and servers)

```
bash -c "$(curl -fsSL https://raw.githubusercontent.com/capstanlqc/omegat_customization/master/custo/omtlinux_custom_installer.sh)"
```

### macOS

Please follow [this guide](https://slides.com/capstan/omegat-installation-and-customization-guide-macos) (originally written for OmegaT version 4.3.2 but it should work too with latest versions).
