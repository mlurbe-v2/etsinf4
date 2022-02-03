----------------------------
--  Ángel Pérez González  --
--  Eva Alcalá López      --
----------------------------

with BB;       use BB;
with BB.Ideal; use BB.Ideal;
with Ada.Real_Time; use Ada.Real_Time;
with Ada.Text_IO; use Ada.Text_IO;
with Ada.Characters.Latin_1; use Ada.Characters.Latin_1;
with Ada.Numerics.Elementary_Functions; use Ada.Numerics.Elementary_Functions;

procedure travel_times_opt is
   told, tnew, tinit : Time;
   tiempoSSO, tiempoExp : Time_Span;
   Posicion : Position;
   --Tiempo que tarda según la fórmula
   theoric_Time : Float;
   error : Float;
   type Gravities is array (Solar_System_Object) of Float;

   Gravity_Of : constant Gravities :=
     (Mercury =>  3.7,    Venus   =>  8.87,  Earth   =>  9.80665,
      Mars    =>  3.711,  Jupiter => 24.79,  Saturn  => 10.4,
      Uranus  =>  8.69,   Neptune => 11.15,  Pluto   =>  0.62,
      Moon    =>  1.6249, Ceres   =>  0.27,  Eris    =>  0.82,
      Vesta   =>  0.22);

   type Fixed is delta 0.000001 range -1.0e6 .. 1.0e6;
begin
   Set_Simulation_Mode (Closed_Loop);
   
   --Mover el ángulo al máximo y esperar a que llegue al final.
   Set_Beam_Angle (Max_Angle);
   loop
      Posicion := Ball_Position;
      exit when Posicion = Min_Position;
   end loop;
   --Tomar tiempo inicial
   told := Clock;
   tinit := told;
   --Para todos los elementos del sistema solar.
   for Location in Solar_System_Object loop

      Move_BB_To (Location);     
      
      --Cambiar el ángulo al mínimo si está en la posicion 
      --mínima o viceversa y esperar al final de la barra.
      if Ball_Position = Min_Position then
         Set_Beam_Angle (Min_Angle);
         loop
            Posicion := Ball_Position;
            exit when Posicion = Max_Position;
         end loop;
      else 
         Set_Beam_Angle (Max_Angle);
         loop
            Posicion := Ball_Position;
            exit when Posicion = Min_Position;
         end loop;
      end if;
      
      --Tomar el tiempo que ha tardado en caer.
      tnew := Clock;
      
      --Tomar tiempo de desplazamiento, calcular la diferencia de tiempo
      --y calcular el tiempo teórico para calcular el error.
         
      tiempoSSO := tnew - told; 
      told := tnew;
      theoric_Time := Sqrt(5.1928/Gravity_Of(Location));
      error := Float(To_Duration(tiempoSSO)) - theoric_Time;
      
      --Mostrar por pantalla el resultado.
        
      Put_Line (Location'Image & ":" & Fixed(To_Duration(tiempoSSO))'Image & " s." & HT &"Error =" & Fixed(error)'Image & " ms");
      
   end loop;
   
   --Mostrar el tiempo total del experimento
   tnew := Clock;
   tiempoExp := tnew - tinit;
   Put_Line ("El tiempo resultante de hacer el experimento ha sido de: " & To_Duration(tiempoExp)'Image & " s.");
end travel_times_opt;
