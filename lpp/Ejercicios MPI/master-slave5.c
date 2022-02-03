#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>

struct tipo_mensaje{
int i;
double a;
int b[10];
};

int main(int argc, char *argv[])
{
    MPI_Init(&argc, &argv);	/* initialize MPI */
    int	rank, nprocs;
    
    MPI_Comm_rank (MPI_COMM_WORLD, &rank);
    MPI_Comm_size (MPI_COMM_WORLD, &nprocs);
    
    if(nprocs != 2 && rank == 0)
    {
        printf("La aplicación necesita 2 procesos\n");
        MPI_Abort(MPI_COMM_WORLD, EXIT_FAILURE);
    }
    
    
    //Se crea el tipo ara MPI y las longitudes de cada variable del struct
    MPI_Datatype message_type;
    int lengths[3] = { 1, 1, 10 };
    
    MPI_Aint displacements[3];
    struct tipo_mensaje mensaje;
    
    //Direcciones de memoria de las variables
    MPI_Aint base_address;
    MPI_Get_address(&mensaje, &base_address);
    MPI_Get_address(&mensaje.i, &displacements[0]);
    MPI_Get_address(&mensaje.a, &displacements[1]);
    MPI_Get_address(&mensaje.b[0], &displacements[2]);
    
    //Desplazamientos en memoria de las variables
    displacements[0] = MPI_Aint_diff(displacements[0], base_address);
    displacements[1] = MPI_Aint_diff(displacements[1], base_address);
    displacements[2] = MPI_Aint_diff(displacements[2], base_address);
    
    //Tipos de las variables
    MPI_Datatype types[3] = { MPI_INT, MPI_DOUBLE, MPI_INT };
    //Se crea el struct
    MPI_Type_create_struct(3, lengths, displacements, types, &message_type);
    MPI_Type_commit(&message_type);
    

    if (rank == 0) {
    //MASTER
        //Se crea el struct con los datos a enviar
        struct tipo_mensaje mensajeMaster;
            mensajeMaster.i = 7;
            mensajeMaster.a = 4.20;
            for(int i = 0; i<10;i++)
                mensajeMaster.b[i] = i+1;
            MPI_Send(&mensajeMaster, 1, message_type, 1, 0, MPI_COMM_WORLD);
    } else {
    //SLAVE
        //Se crea el struct donde se almacenará el struct enviado
         struct tipo_mensaje mensajeSlave;
            MPI_Recv(&mensajeSlave, 1, message_type, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            
            //Se muestran por pantalla los valores
            printf("Mensaje recibido con i= %d y a = %f \nValores de b:\n",mensajeSlave.i,mensajeSlave.a);
            for(int i = 0; i<10;i++)
                printf("%d \n",mensajeSlave.b[i]);
        
    }

    MPI_Type_free(message_type);
    MPI_Finalize();		/* cleanup MPI */
    return 0;
}
