#include <mpi.h> 
#include <stdio.h> 
#include <stdlib.h>
#define NMAX 1000000
#define NREPS 100

int main(int argc,char *argv[])
{
  int mess_size, myid, numprocs, numreps, i, message, len;
  double t1, t2;
  char* name[MPI_MAX_PROCESSOR_NAME];

  MPI_Init(&argc,&argv);
  MPI_Comm_size(MPI_COMM_WORLD,&numprocs);
  MPI_Comm_rank(MPI_COMM_WORLD,&myid);
  MPI_Get_processor_name(name,&len);

  if (argc==3){
        mess_size = atoi(argv[1]);
        numreps = atoi(argv[2]);
  }else{
        mess_size = 1;
        numreps = 100;
  }

  char byte[mess_size];

  if (mess_size<0) MPI_Abort(MPI_COMM_WORLD,MPI_ERR_ARG);

  t1 = MPI_Wtime();
  for(i=0; i<numreps; i++){
      if (myid==0) {
        MPI_Send(byte, mess_size, MPI_CHAR, 1, 1, MPI_COMM_WORLD);
        MPI_Recv(byte, mess_size, MPI_CHAR, MPI_ANY_SOURCE, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
      } else {
        MPI_Recv(byte, mess_size, MPI_CHAR, MPI_ANY_SOURCE, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        MPI_Send(byte, mess_size, MPI_CHAR, 0, 1, MPI_COMM_WORLD);
      }
  }

  t2 = MPI_Wtime();


   printf("Numero de proceso: %d y me estoy ejecutando en el nodo %s\n", myid, name);

  if (myid==0) {
        printf("Cálculo del tiempo entre mensages con %d procesos\n", numprocs);
        printf("Tiempo: %f\n", (t2-t1)/2/numreps);
  }

        MPI_Finalize();
        return 0;
}
