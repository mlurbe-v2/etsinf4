-------------------------------------------------------------------
--                     Display Dot Pattern                       --
--                            Body                               --
--                                                               --
--  Author: Ángel Pérez González                                 --
--  Universitat Politecnica de Valencia                          --
--  Noviembre 2021                                               --
--                                                               --
-------------------------------------------------------------------



with POV_Sim;
use type POV_Sim.Byte;

with Ada.Real_Time; use Ada.Real_Time;

with Ada.Text_IO; use Ada.Text_IO;

procedure display_dot_pattern is
   T, T_Old : Time;
   Intervalo, Intervalo_deg : Time_Span;
   
   -- Se declara la matriz de puntos
   type Dot_Array is array (1 .. 120) of POV_Sim.Byte;
   Dots : Dot_Array; -- Una matriz de puntos
   J : Integer;
   
begin
   Put ("Test starts");

   POV_Sim.Start_Simulation;
   
   -- Se inicializa la matriz de puntos
   for I in Dots'Range loop
      J := (I - 1) mod 8;
      Dots (I) := (2 ** (7 - J)) + (2 ** (J));
   end loop;
   
   -- Se toma el tiempo inicial
   POV_Sim.Wait_Zero_Crossing(T_Old); 
   for I in 1 .. 10 loop
      -- Se toma el tiempo nuevo
      POV_Sim.Wait_Zero_Crossing (T);
      Intervalo := T - T_Old;
      T_Old := T;
      
      Intervalo_deg := Intervalo/360;
      --Esperamos hasta donde empieza visualizador de display (120 grados)
      delay until T + Intervalo_deg * 120;
      
      --Se hace el bucle para llenar todas las columnas
      for X in 121 .. 240 loop
         -- Se dibuja leyendo cada posicion de la matriz de puntos
         POV_Sim.Drive_LEDs (Dots(X - 120));
         --Se espera el tiempo para cada grado (cada columna)
         delay until T + Intervalo_deg*X;
      end loop;
      
      Put (".");

   end loop;

   POV_Sim.Wait_Zero_Crossing (T);

   New_Line;
   Put_Line ("End of test.");
   Put_Line ("Click <Quit> to close GUI connection.");

   POV_Sim.End_Simulation;
end display_dot_pattern;
