#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>

#define ESCLAVO_PREPARADO  0
#define SOLUCION_ENCONTRADA  1
#define FIN  2
#define BUSCAR_PRIMO  3


int num = 0;
int	rank, nprocs, val = 0;
int max_numeros = 1000;
MPI_Status status;


void main(int argc, char *argv[])
{
    double t1, t2;
    int i = 0;
    int esclavos = nprocs -1;
    int encontrado = 0;
    int numPrimos = atoi(argv[1]);
    int continuar,flag, lastProc;
    MPI_Init(&argc, &argv);	/* initialize MPI */
    
    
    
    MPI_Comm_rank (MPI_COMM_WORLD, &rank);
    MPI_Comm_size (MPI_COMM_WORLD, &nprocs);
    
    srand(time(0));
    if(rank == 0) { t1 = MPI_Wtime(); }
    
    while(num < numPrimos){
        continuar = 1;
    if (rank == 0) {
    //MASTER
        i = 0;
        esclavos = nprocs -1;
        encontrado = 0;
        
        while(esclavos > 0 && num<numPrimos){    
            MPI_Recv(&val, 1, MPI_DOUBLE, MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
            lastProc = status.MPI_SOURCE;
            if(status.MPI_TAG == SOLUCION_ENCONTRADA && encontrado == 0){
                printf("- %d Numero primo encontrado: %d\n",num+1,val);
                esclavos--;
                encontrado = 1;
                num++;
            }
            else{
                if(!encontrado && i < max_numeros){
                    
                    val = random() %100001;
                    MPI_Send(&val, 1, MPI_INT, lastProc, BUSCAR_PRIMO, MPI_COMM_WORLD);
                    i++;
                }
                else{
                    MPI_Send(&lastProc, 0, MPI_INT, lastProc, FIN, MPI_COMM_WORLD);
                    esclavos--;
                }
            }
        }
        
        
    } else {
    //SLAVE
        MPI_Send(&val, 1, MPI_INT, 0, ESCLAVO_PREPARADO, MPI_COMM_WORLD);
        continuar = 1;
        flag = 0;
        while(continuar != 0){
            MPI_Recv(&val, 1, MPI_INT, 0, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
            if(status.MPI_TAG == FIN){
                continuar = 0;
                
            }

            else {
                for (int i = 2; i <= val / 2; ++i) {

                    // if n is divisible by i, then n is not prime
                    // change flag to 1 for non-prime number
                    if (val % i == 0) {
                    flag = 1;
                    break;
                    }
                }

                // flag is 0 for prime numbers
                if(flag == 0 && val != 1 && val != 0){
                    MPI_Send(&val, 1, MPI_INT, 0, SOLUCION_ENCONTRADA, MPI_COMM_WORLD);
                    continuar = 0;
                }
                else {
                    MPI_Send(&val, 1, MPI_INT, 0, ESCLAVO_PREPARADO, MPI_COMM_WORLD);
                }
            }
        }
    }

   
    
    }
    MPI_Bcast(&num, 1, MPI_INT, 0, MPI_COMM_WORLD);
    if(rank == 0) {
        t2 = MPI_Wtime();
        t1 = t2 - t1;
        printf("\nTiempo de ejecucion: %f\n\n", t1);
    }
    MPI_Finalize();		/* cleanup MPI */
    
}
