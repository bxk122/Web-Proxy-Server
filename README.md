A <b>proxy server</b> is a server that acts as an intermediary for requests from clients resources from the other servers. A client connects to 
the proxy server, requesting for some service then the proxy server evaluates the request as a way to simplify and control its complexity.
For this project we were required to create a multithreaded proxy server that listens to the clients request, which it forwards to the web
server and eventually respond to it back to the client. The proxy server is also supposed to perform caching along with request forwarding.

In this proxy server, we have a database with the html file of every new website that has been requested along with a checklist of the
file that has the URL of every html page we have. When a client requests a new page the proxy server iterate through the checklist and 
checks if it contains the URL that is being requested. If the list contains the URL  requested  then the proxy server sends the html file
to the client straight from the cache without contacting the original server, else the proxy server contacts the server and sends the 
information to the client after the server sends the data requested. The server periodically updates the database in a time interval of 
5 minutes and it does this by looking through the checklist file and then updates every URL in the checklist file. This way every html 
file stored in the database is updated. However, this code works only on basic html pages and would not work on sites like Facebook or 
YouTube. And we choose our test site as http://borax.truman.edu/310/ and vh216602.truman.edu/agarvey/ because they have basic html format. 
