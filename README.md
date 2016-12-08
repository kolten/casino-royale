# casino-royale
This readme describes how to compile and run group 2 and 4's Casino-Royale project.

[toc]

## Windows ##
----------
This system was developed using **JDK 1.8**.
This system requires that OpenSplice *must* installed in the *C:\HDE\... directory.*
This system requires that environment variables be set up correctly.
To do this, run ```release.bat``` located in `\HDE\x86_64.win64\`.

To compile the program, run ```BUILD_java_EDIT.bat``` located in `\casino-royale\standalone.`

To run a player, run ```RUN_PlayerMain.bat``` located in `\casino-royale\standalone.`

To run a dealer, run ```RUN_DealerMain.bat``` located in `\casino-royale\standalone.`

The above three batch scripts contain all the necessary class paths and arguments.


## Linux ##
----------
1. When first opening the folder, open `javaENV.sh` with the text editor of your choice and find the following line
`export JAVA_HOME=/usr/lib/jvm/@@JAVA_VER@@`
2. Change `@@JAVA_VER@@` to the version of the JDK on your machine.
3. `source SOURCE_THIS_FILE.SH`
4. To run the dealer, type `./RUNDEALER`
5. To run the player, type `./RUNPLAYER`