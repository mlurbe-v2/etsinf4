----------------------------
--  Ángel Pérez González  --
--  Eva Alcalá López      --
----------------------------

with BB; use BB;
with BB.Ideal; use BB.Ideal;
with System; use System;
with BB_PD_Control; use BB_PD_Control;
with Ada.Real_Time; use Ada.Real_Time;
with BB.GUI; use BB.GUI;

procedure seven_targets is
   
   --Array Targets del ejercicio 4
   
   --Targets : constant array (1 .. 7) of Position := 
   --(1 => Max_Position / 4.0,  2 => Min_Position / 4.0, 
   --3 => Max_Position / 2.0,  4 => Min_Position / 2.0, 
   --5 => Max_Position - 10.0, 6 => Min_Position + 10.0, 7 => 0.0);
   
   --Array Targets que pide el entregable
   
   Targets : constant array (1 .. 8) of Position :=
     ( 1 | 3 | 5 | 7 => Max_Position - 10.0,
       2 | 4 | 6 | 8 => Min_Position + 10.0);
   
   task Controller 
     with Priority => System.Priority'Last - 5
   is
      entry Start;
      entry Finish;
   end Controller;

   task body Controller is
      Periodo : Time_Span := Microseconds(62500);
      Next : Time := Clock;
      angulo : Angle;
   begin
      --Tarea bloqueada hasta que se llama a la entry Start
      accept Start;
      
      loop
         select
            --Aceptar entry Finish para terminar la tarea
            accept Finish;
            exit;
         or
              --Tarea periódica
            delay until Next;
            Calculate_Action(Ball_Position, angulo);
            Set_Beam_Angle(angulo);
            Next := Next + Periodo;
            
         end select;
      end loop;
   end Controller;
   

begin
   --Configurar los parametros del controlador
   Configure_PD (Kp     => 1.5,
                 Kd     => 0.9,
                 Target => 0.0);
   
   --Inicio tarea Controller
   Controller.Start;

   --Iterar sobre los targets del array
   for target of Targets loop
      Set_Target_Position(target);
      GUI_Setpoint(target);
      
      delay 6.0;
      
   end loop;
   
   --Terminar la tarea Controller
   Controller.Finish;
end seven_targets;
