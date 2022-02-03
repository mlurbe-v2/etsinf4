-------------------------------------------------------------------
--                     Period Suspension                         --
--                            Body                               --
--                                                               --
--  Author: Ángel Pérez González                                 --
--  Universitat Politecnica de Valencia                          --
--  Noviembre 2021                                               --
--                                                               --
-------------------------------------------------------------------


with POV_Sim; use POV_Sim;
with Ada.Text_IO; use Ada.Text_IO;
with Ada.Real_Time; use Ada.Real_Time;

procedure period_suspension is
   Tiempo,Tiempo_Viejo : Time;
   Intervalo : Time_Span;
begin
   POV_Sim.Start_Simulation;

   -- Esperar a estar en 0 para tomar el tiempo inicial
   Wait_Zero_Crossing(Tiempo_Cero);

   for I in 1 .. 10 loop
      -- Volver a esperar al punto 0 para coger el tiempo en dar una vuelta
      Wait_Zero_Crossing(Tiempo);

      --Calcular tiempo para dar una vuelta
      Intervalo := Tiempo - Tiempo_Viejo;
      Tiempo_Viejo := Tiempo;

      --Mostrar el tiempo por pantalla
      Put_Line(To_Duration(Intervalo)'Image);
   end loop;
   POV_Sim.End_Simulation;
end period_suspension;
