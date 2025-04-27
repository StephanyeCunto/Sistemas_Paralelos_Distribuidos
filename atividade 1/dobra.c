#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main(){
    int num;
    scanf("%i", &num);
    printf("Programa C: mensagem padrao - dobro de %i = %i\n", num, num*2);
    fprintf(stderr, "Programa C: mensagem na saída de erro\n");
    sleep(10);
    return 0;
}