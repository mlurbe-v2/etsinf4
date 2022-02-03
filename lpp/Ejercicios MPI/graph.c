
#include <stdio.h>
#include <mpi.h>

int main( int argc, char *argv[] ) {

  int rank, size;
  MPI_Init( &argc, &argv );
  MPI_Comm_rank( MPI_COMM_WORLD, &rank );
  MPI_Comm_size( MPI_COMM_WORLD, &size );

  /* Lectura de datos de entrada */
  if( size != 12 ) {
    if( !rank ) fprintf(stderr,"%s: Se debe ejecutar con 12 procesos.\n",argv[0]);
    MPI_Finalize();
    return 0;
  }

  int nodes = size;
  int index[] = {3,5,8,11,12,13,16,17,19,20,21,22};
  int edges[] = {1,2,3,0,4,0,5,6,0,7,8,1,2,2,9,10,3,3,11,6,6,8};
  
  MPI_Comm MPI_COMM_GRAPH;
  MPI_Graph_create( MPI_COMM_WORLD, nodes, index, edges, 0, &MPI_COMM_GRAPH );

  int nneighbors;
  const max_neighbors = 3;
  int neighbors[max_neighbors];
  MPI_Graph_neighbors_count( MPI_COMM_GRAPH, rank, &nneighbors );
  MPI_Graph_neighbors( MPI_COMM_GRAPH, rank, max_neighbors, neighbors );
  int mensaje[3];
  int count = 0;
  MPI_Status status;
  int i;
  for( i=0; i<nneighbors; i++ ) {
    if( neighbors[i] < rank ) {
      /* En este caso mi vecino es mi padre. Voy a recibir un mensaje de él */
      MPI_Recv(&mensaje,3,MPI_INT, neighbors[i],0,MPI_COMM_GRAPH,&status);
      MPI_Get_count(&status,MPI_INT, &count); /* Averiguo cuantos elementos tenía el mensaje recibido */
    } else {
      mensaje[count] = rank;
      /* En este caso mis vecinos son hijos míos. Les envío a cada uno un mensaje compuesto por el mensaje recibido más mi rango */
      MPI_Send(mensaje,count+1,MPI_INT,neighbors[i],0,MPI_COMM_GRAPH);
    }
  }
  if( nneighbors == 1 ) {
    /* Su soy una hoja (no tengo hijos), imprimo el valor del mensaje */

	if(count == 2){
    		printf("Soy el nodo %d y los nodos pasados son: [%d, %d]\n",rank,mensaje[0],mensaje[1]);
	}
	else{
		 printf("Soy el nodo %d y los nodos pasados son: [%d, %d, %d]\n",rank,mensaje[0],mensaje[1],mensaje[2]);
	}
  }

  MPI_Finalize();
  return 0;

}

