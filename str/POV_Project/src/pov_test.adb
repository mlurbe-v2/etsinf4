with POV_Sim;
use type POV_Sim.Byte;

with Ada.Real_Time; use Ada.Real_Time;

with Ada.Text_IO; use Ada.Text_IO;

procedure Pov_Test is

      T : Time;

begin

   Put ("Test starts");

   POV_Sim.Start_Simulation;

   for I in 1 .. 8 loop

      POV_Sim.Wait_Zero_Crossing (T);

      delay until T + Milliseconds (970);

      POV_Sim.Drive_LEDs ((2 ** I) - 1);

      Put (".");

   end loop;

   POV_Sim.Wait_Zero_Crossing (T);

   New_Line;
   Put_Line ("End of test.");
   Put_Line ("Click <Quit> to close GUI connection.");

   POV_Sim.End_Simulation;

end Pov_Test;
