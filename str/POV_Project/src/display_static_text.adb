-------------------------------------------------------------------
--                    Display Static Text                        --
--                            Body                               --
--                                                               --
--  Author: Ángel Pérez González                                 --
--  Universitat Politecnica de Valencia                          --
--  Noviembre 2021                                               --
--                                                               --
-------------------------------------------------------------------

with Chars_8x5; use Chars_8x5;

with POV_Sim;
use type POV_Sim.Byte;

with Ada.Real_Time; use Ada.Real_Time;

with Ada.Text_IO; use Ada.Text_IO;

procedure display_static_text is
   
   T, T_Old : Time;
   Intervalo, Intervalo_deg : Time_Span;
   type Dot_Array is array (1 .. 120) of POV_Sim.Byte;
   Puntos : Dot_Array;

   function Translate_String (Text : in String) return Dot_Array is    
      Dots : Dot_Array;
      texto : String := Text;
      espacio : Character := ' ';
   begin
      -- Inicializa todo Dots al caracter en blanco
      for I in 1 .. 120 loop
         Dots(I) := POV_Sim.Byte(Chars_8x5.Char_Map(espacio, 0));
      end loop; 
      -- Para cada caracter de texto 
      for J in 0 .. texto'Length -1 loop
         -- Traducir solo los 20 primeros carácteres
         if J < 20 then
            --Se traduce el caracter a sus respectivas columnas y se meten en la posicion final de Dots
            for X in 0 .. 4 loop
               Dots(J*6 + 1 + X) := POV_Sim.Byte(Chars_8x5.Char_Map(Character(texto(J+1)), X));
            end loop;
            
         end if;
      end loop;
      
      return Dots;
   end Translate_String;
   
begin
   
   Put ("Test starts");

   POV_Sim.Start_Simulation;
   
   -- Se toma el tiempo inicial
   POV_Sim.Wait_Zero_Crossing(T_Old); 
   
   --Dot_Array con la cadena traducida a puntos
   Puntos := Translate_String("Mi nombre es angel");
   
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
         POV_Sim.Drive_LEDs (Puntos(X - 120));
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
   
end display_static_text;
