# CryptoChat

This desktop application lets you start a server and (multiple) client jar files.
You can then write messages between the clients which are sent through the server application.
This all happens with self writte end-to-end encryption. You're also able to see what happens under the hood in a seperate log window. 

## How to use 

* 1.Start ServerMain.jar 
* 2.Start multiple instances of ClientMain.jar

## Built With

* [JavaFX](https://openjfx.io/) - The UI Framework used
* [Heart](https://en.wikipedia.org/wiki/Heart) - The essence for creating this application

## Additional examination 

In case you want to see the actual traffic and packets that get send, Wireshark unfortunately won't help you since you can't examine packets on your loopback interface (at least on windows)
* [RawCap](https://www.netresec.com/?page=rawcap) is going to help you monitor everything that happens on 127.0.0.1 

## Authors

Students of HAW-Hamburg 

* **Simon Hoyos Cadavid** 
* **Max Bauer** 
* **Matthias Pawlitzek** 
