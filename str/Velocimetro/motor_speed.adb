--  Ángel Pérez González

with Motor_Sim;     use Motor_Sim;
with Ada.Text_IO;   use Ada.Text_IO;
with Ada.Real_Time; use Ada.Real_Time;


procedure Motor_Speed is

   --------------------
   --  Objeto Edges  --
   --------------------

   protected Edges is
      --  Declare protected subprograms:
      --      Add_One
      procedure Add_One;
      --      Read_And_Reset
      procedure Read_And_Reset (F : out Integer);
   private
      Flancos : Integer := 0;
   end Edges;

   protected body Edges is

      procedure Add_One is
      begin
         --Sumar 1 a flancos
         Flancos := Flancos + 1;
      end Add_One;

      procedure Read_And_Reset (F : out Integer)  is
      begin
         --Devolver el valor leido de flancos en F y resetear flancos a 0
         F := Flancos;
         Flancos := 0;
      end Read_And_Reset;
   end Edges;



   --------------------
   --  Tarea Sampler --
   --------------------

   task Sampler is
      entry Finish;
   end Sampler;

   task body Sampler is
      Tflanco : Integer;

      --Indica si ha habido un pulso
      Pulso : Boolean;

      Next : Time;
      Periodo : Time_Span;
   begin
      --Calculo del tiempo que hay de flanco a flanco
      Tflanco := (60_000_000 / (Max_RPM * Edges_Per_Turn));
      Pulso := False;

      --Periodo 5 veces menor
      Periodo := (Microseconds(Tflanco)/5);
      Next := Clock;

      loop
         Next:= Next + Periodo;
         select
            --Aceptar entry Finish para terminar la tarea
            accept Finish;
            exit;
         or
              --Tarea periódica
            delay until Next ;

            --Comprobar que ha habido un flanco y sumarlo con Edges.Add_One
            if Motor_Pulse /= Pulso then
               Pulso := not Pulso;
               Edges.Add_One;
            end if;

         end select;

      end loop;
   end Sampler;



   -----------------------
   -- Tarea Speedometer --
   -----------------------

   task Speedometer is
      entry Finish;
      -- Declare Finish
   end Speedometer;
   task body Speedometer is

      --Flancos detectados para determinada velocidad
      FlancosVel : Integer;

      Velocidad : Integer ;
      Next_Time : Time;
      Periodo : Time_Span;

      --Requisito de precisión
      k : Integer;


   begin

      -- Precisión para 5 RPM
      k := 5;

      --Calcular el periodo para poder detectar los flancos de la velocidad máxima
      Periodo := Microseconds(((60_000_000 / Max_RPM) / Edges_Per_Turn) * (Max_RPM/ k));
      Next_Time := Clock;

      loop

         --Actualizar el valor de Next_Time para la tarea periódica
         Next_Time := Next_Time + Periodo;
         select
            --Aceptar entry Finish para terminar la tarea
            accept Finish;

            --Llamar a la entry Finish de Sampler para terminarla
            Sampler.Finish;
            exit;
         or
              --Tarea periódica
            delay until Next_Time;
            --Leer el valor de los flancos detectados y devolverlos en FlancosVel
            Edges.Read_And_Reset(FlancosVel);

            --Determinar la velocidad para una precisión de 5 RPM
            Velocidad := FlancosVel * 5;

            --Mostrar la velocidad
            Put_Line("Speed: " & Velocidad'Image & " RPM");


         end select;

      end loop;
   end Speedometer;


begin

   Put_Line ("Speedometer");
   Put_Line ("-----------");
   Put_Line ("Motor stopped for 5 seconds");
   delay 5.0;

   Put_Line ("10 seconds at full speed");
   Set_Speed (Full);
   Motor_On;
   delay 10.0;

   Put_Line ("10 seconds at half speed");
   Set_Speed (Half);
   delay 10.0;

   Put_Line ("Another 10 seconds at full speed");
   Set_Speed (Full);
   delay 10.0;

   Put_Line ("Stopping motor for 5 seconds");
   Motor_Off;
   delay 5.0;

   Put_Line ("Ending speedometer");
   --  Finishing Speedometer must finish Sampler as well
   Speedometer.Finish;
   Put_Line ("End of main task");

end Motor_Speed;
