----------------------------
--  �ngel P�rez Gonz�lez  --
--  Eva Alcal� L�pez      --
----------------------------

package body BB_PD_Control is

   kpLoc, kdLoc, error, lastError : Float;
   targetLoc : Position;

   ------------------
   -- Configure_PD --
   ------------------

   procedure Configure_PD (Kp, Kd : in Float; Target : in Position := 0.0) is

   begin
      --Se actualizan los valores para kp, kd y target locales.
      kpLoc := Kp;
      kdLoc := Kd;
      targetLoc := Target;
   end Configure_PD;

   -------------------------
   -- Set_Target_Position --
   -------------------------

   procedure Set_Target_Position (Target : in Position) is
   begin
      targetLoc := Target;
   end Set_Target_Position;

   ----------------------
   -- Calculate_Action --
   ----------------------

   procedure Calculate_Action (Pos : in Position; Ang : out Angle) is
         --Variable auxiliar para el �ngulo
         angleAux : Float;
   begin
      --Se calcula el error y la parte proporcional y derivativa
      error := Pos - targetLoc ;

      --Se calcula el �ngulo con la f�rmula
      angleAux := (kpLoc * error) + (kdLoc * (error - lastError));

      --Se comprueba que no se sale del �ngulo m�ximo o m�nimo, y
      --si es as�, se ponen estos como valor.
      if angleAux > Max_Angle then
         Ang := Max_Angle;
      elsif angleAux < Min_Angle then
         Ang := Min_Angle;
      else
         Ang := angleAux;
      end if;

      --Se guarda el anterior error.
      lastError := error;
   end Calculate_Action;

end BB_PD_Control;
