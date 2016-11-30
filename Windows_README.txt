Welcome to Group 2 and 4's Casino Royale Data Package.
Here are some useful commands to help get this project up and running.
Opensplice requires some environmental variables, so run the release.bat located in \HDE\x86_64.win64\.
If the project has not been compiled, run BUILD_java_EDIT.bat located in \casino-royale\standalone.
In order to run a dealer, execute, |java -classpath "C:\HDE\x86_64.win64\jar\dcpssaj.jar";classes DealerMain a c b|
DealerMain requires three arguments to run. The first is a partition name, while the second is a subscriber topic, and the last is a publisher topic.
In order to run a player, execute, |java -classpath "C:\HDE\x86_64.win64\jar\dcpssaj.jar";classes PlayerMain a b c|
PlayerMain requires three arguments to run. The first is a partiont name, this needs to match the dealer's partition name to establish a link.
Second is the player's subscriber topic, this needs to match the dealer's publisher topic.
Last is the player's publisher topic, this needs to match the dealer's subscriber topic.
Both Player and Dealer are autonomus, so sit back and enjoy the show.