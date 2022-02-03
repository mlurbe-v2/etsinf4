-------------------------------------------------------------------
--                      Drive Three Bars                         --
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

procedure drive_three_bars is
   
   T, T_Old : Time;
   Intervalo, Intervalo_deg : Time_Span;
   
begin
      Put ("Test starts");

   POV_Sim.Start_Simulation;
   POV_Sim.Wait_Zero_Crossing(T_Old); 
   for I in 1 .. 10 loop
      --Coger el tiempo que tarda en dar una vuelta
      POV_Sim.Wait_Zero_Crossing (T);
      Intervalo := T - T_Old;
      T_Old := T;
      
      --Conseguir el tiempo que tarda en 1 grado
      Intervalo_deg := Intervalo/360;
      
      -- Esperar hasta el punto inicial del display que se muestra
      delay until T + Intervalo_deg * 120;
      POV_Sim.Drive_LEDs (255);
      
      --Esperarhasta el punto medio
      delay until T + Intervalo_deg * 180;
      POV_Sim.Drive_LEDs (255);
      
      --Esperar hasta el punto final
      delay until T + Intervalo_deg * 239;
      POV_Sim.Drive_LEDs (255);
      
      Put (".");

   end loop;

   POV_Sim.Wait_Zero_Crossing (T);

   New_Line;
   Put_Line ("End of test.");
   Put_Line ("Click <Quit> to close GUI connection.");

   POV_Sim.End_Simulation;
end drive_three_bars;
