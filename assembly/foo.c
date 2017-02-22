#include <errno.h>
#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <netdb.h>
#include <unistd.h>
#include <sys/time.h>

int main() {
    printf("Hei\n");
    printf("SOCK_STREAM: %d\n", SOCK_STREAM);
    printf("AF_INET: %d\n", AF_INET);

    printf("SHUT_RDWR %d\n", SHUT_RDWR);
    printf("a%ld\n", sizeof(struct sockaddr));
    printf("b%ld\n", sizeof(struct sockaddr_in));
    printf("fds%ld\n", sizeof(socklen_t));
    printf("AF_UNSPEC: %d\n", AF_UNSPEC);
    printf("timeval size: %ld\n", sizeof(time_t));
    printf("timezone size: %ld\n", sizeof(struct timezone));
    printf("int size: %ld\n", sizeof(int));
}

void startServer() {
    struct sockaddr_in serverAddress, clientAddress;
    /* Initialize socket structure */
    bzero((char *) &serverAddress, sizeof(serverAddress));
    int port = 11111;

    int serverFd = socket(AF_INET, SOCK_STREAM, AF_UNSPEC);
    printf("%d\n", serverFd);

    serverAddress.sin_family = AF_INET;
    serverAddress.sin_addr.s_addr = INADDR_ANY;
    serverAddress.sin_port = htons(port);

    int tries = 0;
    while (bind(serverFd,
            (struct sockaddr *) &serverAddress,
            sizeof(serverAddress)) != 0 && ++tries < 100) {

        printf("bind() errno: %s\n", strerror(errno));
        sleep(10);
    }

    printf("Accepting connections.\n");

    listen(serverFd, 0);
    int clientLength = sizeof(clientAddress);
   
    /* Accept actual connection from the client */
    int clientFd = accept(serverFd,
                          (struct sockaddr *) &clientAddress,
                          (socklen_t *) &clientLength);
    
    if (clientFd < 0) {
        perror("ERROR on accept");
        exit(1);
    }

    char buffer[256];
    /* If connection is established then start communicating */
    bzero(buffer,256);
    int n = read(clientFd, buffer, 255);
   
    if (n < 0) {
        perror("ERROR reading from socket");
        exit(1);
    }
   
    printf("Here is the message: %s\n",buffer);
   
    /* Write a response to the client */
    n = write(clientFd,"I got your message",18);
   
    if (n < 0) {
        perror("ERROR writing to socket");
        exit(1);
    }

    printf("sc %d", shutdown(clientFd, SHUT_RDWR));

    int closed = close(serverFd);
    if (closed != 0)
        printf("close server: %d", closed);
   
    int closedClient = close(clientFd);
    if (closedClient != 0)
        printf("close client: %d", closedClient);
} 


