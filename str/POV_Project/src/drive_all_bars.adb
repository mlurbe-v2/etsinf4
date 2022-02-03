-------------------------------------------------------------------
--                      Display All Bars                         --
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

procedure drive_all_bars is
   
   T, T_Old : Time;
   Intervalo, Intervalo_deg : Time_Span;
   
begin
      Put ("Test starts");

   POV_Sim.Start_Simulation;
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
      for J in 120 .. 240 loop
         POV_Sim.Drive_LEDs (255);
         --Se espera el tiempo para cada grado (cada columna)
         delay until T + Intervalo_deg*J;
      end loop;
      
      Put (".");

   end loop;

   POV_Sim.Wait_Zero_Crossing (T);

   New_Line;
   Put_Line ("End of test.");
   Put_Line ("Click <Quit> to close GUI connection.");

   POV_Sim.End_Simulation;
end drive_all_bars;
