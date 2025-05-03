#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>

void trata_sinal(int sinal) {
    printf("Sinal recebido %d. Encerrando... \n", sinal);
    exit(0);
}

int main() {
    signal(SIGINT, trata_sinal);

    while (1) {
        printf("Loop...\n");
        sleep(1);
    }

    return 0;
}