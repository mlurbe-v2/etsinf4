-------------------------------------------------------------------
--                      Period Busy Wait                         --
--                            Body                               --
--                                                               --
--  Author: Ángel Pérez González                                 --
--  Universitat Politecnica de Valencia                          --
--  Noviembre 2021                                               --
--                                                               --
-------------------------------------------------------------------

with Ada.Real_Time; use Ada.Real_Time;
with Ada.Text_IO; use Ada.Text_IO;
with POV_Sim; use POV_Sim;

   
procedure period_busy_wait is

   OldTime, NewTime : Time;
   Interval : Time_Span;
begin

   POV_Sim.Start_Simulation;
   
   -- Esperar flanco ascendente en Home_Sensor (T1)
   while Home_Sensor loop
      null;
   end loop;
   while not Home_Sensor loop
         null;
   end loop;
   OldTime := Clock;
   for I in 1 .. 16 loop
      --Esperar flanco ascendente en Home_Sensor (T2)
      while Home_Sensor loop
         null;
      end loop;
      
      while not Home_Sensor loop
         null;
      end loop;
      NewTime := Clock;

      Interval := NewTime - OldTime;
      OldTime := NewTime;
      Put_Line(To_Duration(Interval)'Image);
   end loop;
   POV_Sim.End_Simulation;
end period_busy_wait;

