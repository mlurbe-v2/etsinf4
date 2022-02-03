-------------------------------------------------------------------
--                     Display with Scroll                       --
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
with Ada.Strings.Unbounded; use Ada.Strings.Unbounded;

procedure display_with_scroll is

   --Toma de tiempo
   T, T_Old : Time;
   Intervalo, Intervalo_deg : Time_Span;
   
   --Array de puntos
   type Dot_Array is array (Integer range <>) of POV_Sim.Byte;
   
   --Fichero
   F         : File_Type;
   File_Name : constant String := "texto"; --Ruta del fichero, por defecto en la carpeta raiz del proyecto
   Indice : Integer;
   
   
   
   function Translate_String (Text : in String) return Dot_Array is    
      Dots : Dot_Array(1 .. Text'Length *6 + 240);
      texto : String := Text;
      espacio : Character := ' ';
   begin
      -- Inicializa todo Dots al caracter en blanco
      for I in 1 .. Text'Length *6 + 240 loop
         Dots(I) := POV_Sim.Byte(Chars_8x5.Char_Map(espacio, 0));
      end loop; 
      -- Para cada caracter de texto 
      for J in 0 .. texto'Length -1 loop
            --Se traduce el caracter a sus respectivas columnas y se meten en la posicion final de Dots
            for X in 0 .. 4 loop
               Dots(J*6 + 1 + X + 120) := POV_Sim.Byte(Chars_8x5.Char_Map(Character(texto(J+1)), X));
            end loop;      
      end loop;
      
      return Dots;
   end Translate_String;
   
   
   
   
begin
   
   Put ("Test starts");

   POV_Sim.Start_Simulation;
   
   -- Se toma el tiempo inicial
   POV_Sim.Wait_Zero_Crossing(T_Old);
   
   --Se abre el fichero texto
   Open(F, In_File, File_Name);
   
   --Se hace un declare para tener el string del tamaño de la linea
   declare
      linea : String := Get_Line (F);
   begin    
      Indice := linea'Length * 6;
      --Declare para hacer que Dot_Array tenga el tamaño necesario
      declare
         Puntos : Dot_Array(1 .. Indice +240);
      begin
         
         --Se traduce el string linea a un Dot_Array (Puntos)
         Puntos := Translate_String(linea);
         
         --Se itera para cada elemento de Puntos sin contar los 120 iniciales en blanco
         for I in 0 .. Puntos'Length -120 loop
      
      
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
               POV_Sim.Drive_LEDs (Puntos(X - 120 + I));
               --Se espera el tiempo para cada grado (cada columna)
               delay until T + Intervalo_deg*X;
            end loop;
      
            Put (".");

         end loop;

         POV_Sim.Wait_Zero_Crossing (T);

         New_Line;
         Put_Line ("End of test.");
         Put_Line ("Click <Quit> to close GUI connection.");
      end;
   end;
   Close(F);
   
   
   
  

   POV_Sim.End_Simulation;
end display_with_scroll;
