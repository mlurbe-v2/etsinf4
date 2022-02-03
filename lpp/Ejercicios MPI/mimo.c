#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <omp.h>
#include <mpi.h>

#define	R(i,j)	R[j*lda+i]
#define	b(i)	b[i]
#define	x(i)	x[i]
#define	I(i)	I[i]
#define	sol(i)	sol[i]

#define PREPARADO  0
#define SOLUCION_ENCONTRADA  1
#define FIN  2
#define TAREA  3

#define TAM_BUFFER 80000

void printMatrix( const char *s, double *R, int lda, int n );
void algoritmo1( int n, double *x, int t, double *I, double *R, int lda, double *b, double nrm );
void maestro( int n, double *x, int t, double *I, double *R, int lda, double *b, double nrm, int profundidad);

double minimo, min_buf;
double *sol, *sol_buf;

MPI_Status status;
char buffer[TAM_BUFFER];
//int posicion = 0; 
int msg_size = 0; //Pedro: Cambiado posicion por msg_size por ser más adecuado.


#ifdef CONTAR_TAREAS
int tareas=0;
#endif

int N;

int main( int argc, char *argv[] ) {
int n, i, j, t;
double *R, *I, *b, *x;

if( argc<3 ) {
    fprintf(stderr,"usage: %s n(tamano de la matriz) t(tamano del conjunto discreto) \n",argv[0]);
    exit(-1);
}
sscanf(argv[1],"%d",&n);
/* Reservamos espacio para una matriz cuadrada */
int lda = n;
N = n*n;
if( ( R = (double*) malloc(N*sizeof(double)) ) == NULL ) {
    fprintf(stderr,"Error en la reserva de memoria para la matriz A\n");
    exit(-1);
}
srand(1234);
/* Generamos aleatoriamente la parte triangular superior */
for( i=0; i<n; i++ ) {
    for( j=i; j<n; j++ ) {
    R(i,j) = ((double) rand()) / RAND_MAX;
    }
}

#ifdef DEBUG
/* Imprimir matriz */
printMatrix( "R = ", R, lda, n );
#endif

/* Reservamos espacio para el vector independiente */
if( ( b = (double*) malloc(n*sizeof(double)) ) == NULL ) {
    fprintf(stderr,"Error en la reserva de memoria para el vector b\n");
    exit(-1);
}
/* Generamos aleatoriamente el vector independiente */
for( i=0; i<n; i++ ) {
    b(i) = ((double) rand()) / RAND_MAX;
}

#ifdef DEBUG
/* Imprimir vector */
printf("b = [");
for( i=0; i<n; i++ ) {
    printf("%10.4lf",b(i));
}
printf("    ] \n");
#endif

/* Leemos por teclado el cardinal del conjunto discreto de elementos */
sscanf(argv[2],"%d",&t);
if( t%2 ) {
    fprintf(stderr,"El valor de t ha de ser par \n");
    exit(-1);
}
/* Reservamos espacio para el vector de elementos del conjunto discreto */
if( ( I = (double*) malloc(t*sizeof(double)) ) == NULL ) {
    fprintf(stderr,"Error en la reserva de memoria para el conjunto A\n");
    exit(-1);
}
for( i=0; i<t; i++ ) {
    I(i) = (2*i+1-t)/2.0;
}
#ifdef DEBUG
/* Imprimir conjunto */
printf("I = (");
for( i=0; i<t; i++ ) {
    printf("%10.2lf",I(i));
}
printf("    )\n");
#endif

/* Reservamos espacio para el vector solucion */
if( ( x = (double*) malloc(n*sizeof(double)) ) == NULL ) {
    fprintf(stderr,"Error en la reserva de memoria para el vector x\n");
    exit(-1);
}

/* Llamada a la funcion que minimiza el problema de minimos cuadrados */

double t1 = omp_get_wtime();
    MPI_Init(&argc, &argv);	/* initialize MPI */
    int	rank, nprocs,tareas, continuar = 0;
    minimo = 1.0/0.0; //(double) RAND_MAX;
    sol = (double*) malloc(n*sizeof(double));
    sol_buf = (double*) malloc(n*sizeof(double));

    double nrm = 0.0;
    
    
    MPI_Comm_rank (MPI_COMM_WORLD, &rank);
    MPI_Comm_size (MPI_COMM_WORLD, &nprocs);
    tareas = nprocs -1;
    
if(rank == 0){
    maestro( n, x, t, I, R, lda, b, nrm, 4 );
    while(tareas>0){
        MPI_Recv(&buffer, TAM_BUFFER, MPI_PACKED, MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
        
        if(status.MPI_TAG == SOLUCION_ENCONTRADA){
            msg_size = 0;
                        MPI_Unpack(buffer,TAM_BUFFER,&msg_size,sol_buf,n,MPI_DOUBLE,MPI_COMM_WORLD); //Pedro: sol_buf ya es un puntero, no debe ponerse & delante
                        MPI_Unpack(buffer,TAM_BUFFER,&msg_size,&min_buf,1,MPI_DOUBLE,MPI_COMM_WORLD);
                if(min_buf < minimo) {
                            minimo = min_buf; 
                            memcpy(sol,sol_buf,n*sizeof(double));//El memcpy estaba al revés 
                        }
        }
        MPI_Send(&buffer,msg_size,MPI_PACKED,status.MPI_SOURCE,FIN,MPI_COMM_WORLD); //Pedro: Se envían solo los bytes necesarios
        //Pedro: Hay que decrementar el número de tareas cada vez que se envía un mensaje de FIN
        tareas--;
    }
    }
    else {
        MPI_Send(&buffer,0,MPI_PACKED,0,PREPARADO,MPI_COMM_WORLD);
        continuar = 1;
        while(continuar != 0) {
            MPI_Recv(&buffer, TAM_BUFFER, MPI_PACKED, 0, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
            if(status.MPI_TAG == FIN){
                continuar = 0;
            }
            else{
                msg_size = 0;

            //UNPACK para los datos del algoritmo1
            
            MPI_Unpack(buffer,TAM_BUFFER,&msg_size,&n,1,MPI_INT,MPI_COMM_WORLD); 
            MPI_Unpack(buffer,TAM_BUFFER,&msg_size,sol,lda,MPI_DOUBLE,MPI_COMM_WORLD); //El tamaño de sol tiene que ser de lda, no n
            MPI_Unpack(buffer,TAM_BUFFER,&msg_size,&minimo,1,MPI_DOUBLE,MPI_COMM_WORLD);
            MPI_Unpack(buffer,TAM_BUFFER,&msg_size,x,lda,MPI_DOUBLE,MPI_COMM_WORLD); 
            MPI_Unpack(buffer,TAM_BUFFER,&msg_size,R,N,MPI_DOUBLE,MPI_COMM_WORLD);
            MPI_Unpack(buffer,TAM_BUFFER,&msg_size,b,n,MPI_DOUBLE,MPI_COMM_WORLD);
            MPI_Unpack(buffer,TAM_BUFFER,&msg_size,&nrm,1,MPI_DOUBLE,MPI_COMM_WORLD);
                
//                 #pragma omp parallel
//                 #pragma omp single
                algoritmo1( n, x, t, I, R, lda, b, nrm );
                
                msg_size = 0;
                    MPI_Pack(sol,lda,MPI_DOUBLE,buffer,TAM_BUFFER,&msg_size,MPI_COMM_WORLD); //El tamaño de sol tiene que ser lda, no n
                    MPI_Pack(&minimo,1,MPI_DOUBLE,buffer,TAM_BUFFER,&msg_size,MPI_COMM_WORLD);

                MPI_Send(&buffer,msg_size,MPI_PACKED,0,SOLUCION_ENCONTRADA,MPI_COMM_WORLD); //Pedro: Se envían solo los bytes necesarios
            }
        }
    }
if(rank == 0){
double t2 = omp_get_wtime();
printf("Tiempo = %lf\n",t2-t1);

printf("Minimo = %lf\n",minimo);
printf("sqrt(minimo) = %lf\n",sqrt(minimo));
printf("x = (");
for( i=0; i<n; i++ ) {
    printf("%10.2lf",sol(i));
}
printf("    )\n");
}
#ifdef CONTAR_TAREAS
printf("tareas = %d\n",tareas);
#endif

MPI_Finalize();
free(x);
free(I);
free(R);
free(b);
free(sol);
free(sol_buf);
}

void printMatrix( const char *s, double *R, int lda, int n ) {
int i, j;
for( i=0; i<n; i++ ) {
    for( j=0; j<i; j++ ) {
    printf("%10.4lf",0.0);
    }
    for( j=i; j<n; j++ ) {
    printf("%10.4lf",R(i,j));
    }
    printf("\n");
}
}

/**
    Algoritmo1 del enunciado de la práctica en el que se trabaj con el cuadrado de la norma y se va calculando según    .
    se sugiere en la misma memoria.

    n	(int)		Tamaño del subvector de x sobre el que se está trabajando, x(0:n-1). Las componentes x(n:lda) se han calculado ya.
    x	(double*)	Puntero al primer elemento del vector x (completo).
    t	(int)		Tamaño del conjunto discreto I de símbolos.
    I	(double*)	Puntero al primer elemento del vector donde se encuentran los elementos del conjunto discreto I.
    R	(double*)	Puntero al primer elemento de la matriz triangular superior R de tamaño lda x n.
    lda	(int)		(Leading dimension) Número de filas de la matriz R. Coincide con el tamaño de los vectores x y b en la primera llamada a la función.
    b	(double*)	Puntero al primer elemento del vector b (completo).
    nrm	(double)	Norma calculada hasta el momento de la llamada a este algoritmo.

    El vector x es de entrada/salida, los demás argumentos son solo de entrada. 
*/
void algoritmo1( int n, double *x, int t, double *I, double *R, int lda, double *b, double nrm ) {
int k, inc = 1;
if( n==0 ) {
    if( nrm < minimo ) {
    minimo = nrm;
    dcopy_( &lda, x, &inc, sol, &inc );
    }
} else {
    for( k=0; k<t; k++ ) {
    int m = n-1;
    x(m) = I(k);
    double r = R(m,m)*x(m) - b(m);
    double norma = nrm + r*r; 
    if( norma < minimo ) {
        double v[m];
        double y[lda]; 
        dcopy_( &m, b, &inc, v, &inc );
        dcopy_( &lda, x, &inc, y, &inc );
        double alpha = -x(m);
        daxpy_( &m, &alpha, &R(0,m), &inc, v, &inc );
        algoritmo1( m, y, t, I, R, lda, v, norma );
    }
    }
}
}

//Maestro
void maestro( int n, double *x, int t, double *I, double *R, int lda, double *b, double nrm, int profundidad ) {
int k, inc = 1;
if( n==0 ) {
    if( nrm < minimo ) {
    minimo = nrm;
    dcopy_( &lda, x, &inc, sol, &inc );
    }
} else {
    for( k=0; k<t; k++ ) {
    int m = n-1;
    x(m) = I(k);
    double r = R(m,m)*x(m) - b(m);
    double norma = nrm + r*r; 
    if( norma < minimo ) {
        double v[m];
        double y[lda]; 
        dcopy_( &m, b, &inc, v, &inc );
        dcopy_( &lda, x, &inc, y, &inc );
        double alpha = -x(m);
        daxpy_( &m, &alpha, &R(0,m), &inc, v, &inc );
        if(profundidad > 0){
            maestro(m, y, t, I, R, lda, v, norma, profundidad-1);
        }
        else{
            MPI_Recv(&buffer, TAM_BUFFER, MPI_PACKED, MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
            
            if(status.MPI_TAG == SOLUCION_ENCONTRADA){
            msg_size = 0;
                        MPI_Unpack(buffer,TAM_BUFFER,&msg_size,sol_buf,lda,MPI_DOUBLE,MPI_COMM_WORLD); //Pedro: sol_buf ya es un puntero, no debe ponerse & delante
                        MPI_Unpack(buffer,TAM_BUFFER,&msg_size,&min_buf,1,MPI_DOUBLE,MPI_COMM_WORLD);
                if(min_buf < minimo) {
                    
                            minimo = min_buf;
                            memcpy(sol,sol_buf,lda*sizeof(double));//El memcpy estaba al revés y el sizeof tiene que ser de lda, no de la n modificada
                        }
            }
            
            msg_size = 0;
                    //No hace falta enviar lda, I y t porque no cambian
                    MPI_Pack(&m,1,MPI_INT,buffer,TAM_BUFFER,&msg_size,MPI_COMM_WORLD);
                    MPI_Pack(sol,lda,MPI_DOUBLE,buffer,TAM_BUFFER,&msg_size,MPI_COMM_WORLD); 
                    MPI_Pack(&minimo,1,MPI_DOUBLE,buffer,TAM_BUFFER,&msg_size,MPI_COMM_WORLD);
                    MPI_Pack(y,lda,MPI_DOUBLE,buffer,TAM_BUFFER,&msg_size,MPI_COMM_WORLD); 
                    MPI_Pack(R,N,MPI_DOUBLE,buffer,TAM_BUFFER,&msg_size,MPI_COMM_WORLD); 
                    MPI_Pack(v,m,MPI_DOUBLE,buffer,TAM_BUFFER,&msg_size,MPI_COMM_WORLD); 
                    MPI_Pack(&norma,1,MPI_DOUBLE,buffer,TAM_BUFFER,&msg_size,MPI_COMM_WORLD);
                    
                    //Enviar a dicho Esclavo los datos para ejecutar algoritmo1
                    MPI_Send(buffer,TAM_BUFFER,MPI_PACKED,status.MPI_SOURCE,TAREA,MPI_COMM_WORLD);
        }
        
    }
    }
}
}
