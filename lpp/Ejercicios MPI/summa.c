
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
//#include <mkl_blas.h>
#include <mpi.h>

#define	TRUE	1
#define	FALSE	0
#define TAM_BUFFER 8000


int main( int argc, char *argv[] ) {

  int rank, size, msg_size;
  char buffer[TAM_BUFFER];
  MPI_Init( &argc, &argv );
  MPI_Comm_rank( MPI_COMM_WORLD, &rank );
  MPI_Comm_size( MPI_COMM_WORLD, &size );

  /* Lectura de datos de entrada */
  if( size != 4 ) {
    if( !rank ) fprintf(stderr,"%s: Se debe ejecutar con 4 procesos.\n",argv[0]);
    MPI_Finalize();
    return 0;
  } 

  /* Generación de datos */
  double A0[4];
  double B0[4]; 
  double C0[4]; 
  MPI_Comm old_comm , comm2D, commrow, commcol;
  int ndims , reorder ,id, periods [2] , dim_size [2], belongs[2], coords2D[2];
  
  if( !rank ) {
    A0[0] = 6.0;
    A0[1] = 0.2;
    A0[2] = 5.8;
    A0[3] = 8.0;
    B0[0] = 0.2;
    B0[1] = 3.7;
    B0[2] = 9.4;
    B0[3] = 6.8;
  }

  /********************************/
  /* Creación del grid cartesiano */
  /********************************/
  old_comm = MPI_COMM_WORLD ;
  ndims = 2; /* Malla 2D */
  dim_size [0] = 2; /* filas */
  dim_size [1] = 2; /* columnas */
  periods [0] = 1; /* periodico en filas */
  /* ( cada columna forma un anillo ) */
  periods [1] = 0; /* no periodico para columnas */
  reorder = 1; /* permite reordenacion */
  
  MPI_Cart_create ( old_comm ,ndims ,dim_size ,periods ,reorder ,&comm2D );


  /*************************************************/
  /* División en comunicadores de filas y columnas */
  /*************************************************/
  MPI_Comm_rank(comm2D, &id);
  MPI_Cart_coords ( comm2D , id , ndims , coords2D );
  /* Creacion de submallas 1D para las filas */
  belongs [0] = 0;
  belongs [1] = 1;
  MPI_Cart_sub ( comm2D , belongs , &commrow );
  /* Creacion de submallas 1D para las columnas */
  belongs [0] = 1;
  belongs [1] = 0;
  MPI_Cart_sub ( comm2D , belongs , &commcol );
 

  /*************************************/
  /* Envío de datos a los procesadores */
  /*************************************/
  double colum[4];
  double datos[2];
  int idrow, idcol;
  double A, B;
  MPI_Status status;
  /* Paso 1: El P00 envía las columnas a su fila de procesos */
  MPI_Comm_rank(commcol, &idcol);
  MPI_Comm_rank(commrow, &idrow);
  
  
  if(id == 0){

        msg_size = 0;
        MPI_Pack(&A0[2],1,MPI_DOUBLE,buffer,TAM_BUFFER,&msg_size,commrow);
        MPI_Pack(&A0[3],1,MPI_DOUBLE,buffer,TAM_BUFFER,&msg_size,commrow);
        MPI_Pack(&B0[2],1,MPI_DOUBLE,buffer,TAM_BUFFER,&msg_size,commrow);
        MPI_Pack(&B0[3],1,MPI_DOUBLE,buffer,TAM_BUFFER,&msg_size,commrow);
        MPI_Send(buffer, TAM_BUFFER, MPI_PACKED, 1, 0, commrow);
        
        A = A0[0];
        B = B0[0];
  }
  else if(id == 1) {
        MPI_Recv(&buffer, TAM_BUFFER, MPI_PACKED, 0, 0, commrow, MPI_STATUS_IGNORE);
        
        msg_size = 0;
        MPI_Unpack(buffer,TAM_BUFFER,&msg_size,&A0[2],1,MPI_DOUBLE,commrow);
        MPI_Unpack(buffer,TAM_BUFFER,&msg_size,&A0[3],1,MPI_DOUBLE,commrow);
        MPI_Unpack(buffer,TAM_BUFFER,&msg_size,&B0[2],1,MPI_DOUBLE,commrow);
        MPI_Unpack(buffer,TAM_BUFFER,&msg_size,&B0[3],1,MPI_DOUBLE,commrow);
        
        A = A0[2];
        B = B0[2];
        
  }
  /* Paso 2: Los procesos de la primera fila envían datos al resto de su misma columna */
  
  if(idcol == 0){
        if(id == 0){
            msg_size = 0;
            MPI_Pack(&A0[1],1,MPI_DOUBLE,buffer,TAM_BUFFER,&msg_size,commcol);
            MPI_Pack(&B0[1],1,MPI_DOUBLE,buffer,TAM_BUFFER,&msg_size,commcol);
            

            MPI_Send(buffer, TAM_BUFFER, MPI_PACKED, 1, 0, commcol);
        }
        else{
           msg_size = 0;
           MPI_Pack(&A0[3],1,MPI_DOUBLE,buffer,TAM_BUFFER,&msg_size,commcol);
           MPI_Pack(&B0[3],1,MPI_DOUBLE,buffer,TAM_BUFFER,&msg_size,commcol);

           MPI_Send(buffer, TAM_BUFFER, MPI_PACKED, 1, 0, commcol); 
        }
        
  }
  else{
        MPI_Recv(&buffer, TAM_BUFFER, MPI_PACKED, 0, 0, commcol, &status);
        
        if(id == 2 ){
            msg_size = 0;
        MPI_Unpack(buffer,TAM_BUFFER,&msg_size,&A0[1],1,MPI_DOUBLE,commcol);
        MPI_Unpack(buffer,TAM_BUFFER,&msg_size,&B0[1],1,MPI_DOUBLE,commcol);
        A = A0[1];
        B = B0[1];
        }
        else if(id == 3 ){
            msg_size = 0;
        MPI_Unpack(buffer,TAM_BUFFER,&msg_size,&A0[3],1,MPI_DOUBLE,commcol);
        MPI_Unpack(buffer,TAM_BUFFER,&msg_size,&B0[3],1,MPI_DOUBLE,commcol);   
        A = A0[3];
        B = B0[3];
        }
        

        
  }  
  
  /******************************/
  /* Algoritmo de multiplicción */
  /******************************/
  int k;
  double C = 0.0;
  for( k = 0; k < 2; k++ ) {
    /* Broadcast de la columna k */
    double a = A;
    MPI_Bcast(&a, 1, MPI_DOUBLE, k, commrow);
    MPI_Barrier(commrow);
    /* Broadcast de la fila */
    double b = B;
    MPI_Bcast(&b, 1, MPI_DOUBLE, k, commcol);
    /* Actualización del elemento C */
    MPI_Barrier(commcol);
    C = C + a*b; 
  }
  if(id == 0){
    C0[id] = C;
  }
  /********************************/
  /* Devuelta de los datos al P00 */
  /********************************/
  /* Paso 1: Todos los procesos envían sus datos al proceso de la primera fila */
     if(idcol == 0){
        if(id == 0){
            MPI_Recv(&C0[1],1,MPI_DOUBLE,1,0,commcol,MPI_STATUS_IGNORE);
        }
        else if (id == 1){
           MPI_Recv(&C0[3],1,MPI_DOUBLE,1,0,commcol,MPI_STATUS_IGNORE); 
        }
        
  }
  else{
        
        if(id == 2 ){
            MPI_Send(&C,1,MPI_DOUBLE,0,0,commcol);
        }
        else if(id == 3 ){
            MPI_Send(&C,1,MPI_DOUBLE,0,0,commcol);
        }
        

        
  } 
  /* Paso 2: El P00 recoge las columnas de su fila de procesos */
 if(id == 0){
        MPI_Recv(&buffer, TAM_BUFFER, MPI_PACKED, 1, 0, commrow, MPI_STATUS_IGNORE);
        
        msg_size = 0;
        MPI_Unpack(buffer,TAM_BUFFER,&msg_size,&C0[2],1,MPI_DOUBLE,commrow);
        MPI_Unpack(buffer,TAM_BUFFER,&msg_size,&C0[3],1,MPI_DOUBLE,commrow);
     
     
  }
  else if(id == 1) {
        
        msg_size = 0;
        MPI_Pack(&C,1,MPI_DOUBLE,buffer,TAM_BUFFER,&msg_size,commrow);
        MPI_Pack(&C0[3],1,MPI_DOUBLE,buffer,TAM_BUFFER,&msg_size,commrow);
        MPI_Send(buffer, TAM_BUFFER, MPI_PACKED, 0, 0, commrow);
        
  }

  /* Impresión del resultado */
  if( !rank ) printf("C = ( %.2f %.2f \n      %.2f %.2f )\n",C0[0],C0[2],C0[1],C0[3]);
  /* Resultado = 
   22.66 95.84
   29.64 56.28
  */

  MPI_Finalize();
  return 0;
}


