/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.ColorSensorColor;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.subsystems.WheelManipulator;
import edu.wpi.first.wpilibj.DriverStation;

import java.util.Arrays;


/**
 * Rotation and position control using a colour sensor for the control panel.
 */
public class WheelOfFortune extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  // VideoCapture camera;

  private boolean doingRotation;
  private boolean doingPosition;

  private int[] countedColors;
  private ColorSensorColor lastDetectedColor;
  private ColorSensorColor colorForPosition;
  private final WheelManipulator subsystem;

  /**
   * 
   * @param subsystem the subsystem for this command (the wheelManipulator class)
   * Also initializez a bunch of variables to be used in the automatic whell of fortion spinning
   */
  public WheelOfFortune(WheelManipulator subsystem) {
    this.subsystem = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
    doingRotation = false;
    doingPosition = false;
    countedColors = new int[4];//0=R,1=Y,2=B,3=G
    Arrays.fill(countedColors, 0);
    lastDetectedColor = ColorSensorColor.none;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    colorForPosition = ColorSensorColor.none;
  }

  
  /**
   * Gets the color value from the frc driver station
   * If the right button 8 is pressed starts rotation control if it hasn't already been done
   * 
   * For the rotation control: waits to turn 3.5ish times
   * For the position control: moves the wheel to be 90 degrees with the given colour (provided by the frc station)
   */
  @Override
  public void execute() {
    //check if we've received anything from the Driver Station
    if(colorForPosition==ColorSensorColor.none){
      String gameData;
      gameData = DriverStation.getInstance().getGameSpecificMessage();
      if(gameData.length() > 0)
      {
        System.out.println("We received data: "+gameData);
        switch (gameData.charAt(0)){//colors are offset because we detect from the middle of the wheel, but the field sensor is on the side. So in theory, if we see blue, the field senses red, etc.
          case 'B' :
            colorForPosition = ColorSensorColor.red;
            break;
          case 'G' :
            colorForPosition = ColorSensorColor.yellow;
            break;
          case 'R' :
            colorForPosition = ColorSensorColor.blue;
            break;
          case 'Y' :
            colorForPosition = ColorSensorColor.green;
            break;
          default :
            colorForPosition = ColorSensorColor.none;//data is corrupt
            break;
        }
      }
    }
    boolean rightButton8 = OI.rightJoystick.getRawButtonPressed(8);
    boolean rightButton9 = OI.rightJoystick.getRawButtonPressed(9);
    //button checking to activate the two modes
    if(rightButton8 && !doingPosition && !doingRotation){//some button on the joystick/controller for Rotation control
      System.out.println("Starting rotational control...");
      doingRotation = true;
      lastDetectedColor = ColorSensorColor.none;
      for(int i=0; i<countedColors.length; i++){
        countedColors[i]=0;
      }
    }
    else if (rightButton9 && !doingPosition && !doingRotation){//some button to activate for Position control
      if(colorForPosition!=ColorSensorColor.none){
        System.out.println("Starting position control...");
        doingPosition = true;
      }
      else{
        System.out.println("No color received from driver station yet");
      }
    }
    else if(rightButton8 && doingRotation && !doingPosition){
      doingRotation = false;
      subsystem.getSpinnerMotor().set(ControlMode.PercentOutput,0.0);
      System.out.println("Stopped rotation control");
    }
    else if(rightButton9 && !doingRotation && doingPosition){
      doingPosition = false;
      subsystem.getSpinnerMotor().set(ControlMode.PercentOutput,0.0);
      System.out.println("Stopped position control");
    }

    ColorSensorColor detectedColor = subsystem.detectColor();
    // System.out.println(detectedColor);
    //the actual logic for the wheels to work
    if(doingRotation && doingPosition){
      //This should NEVER get triggered, if it does I'm probably an idiot but I'm putting it here just in case I screw something up.
      doingPosition = false;
      doingRotation = false;
    }
    else if(doingRotation){
      subsystem.getSpinnerMotor().set(ControlMode.PercentOutput,1.0);
      System.out.println(detectedColor);
      if(detectedColor!=lastDetectedColor){//The color has changed
        lastDetectedColor = detectedColor;
        System.out.println(detectedColor);
        if(detectedColor!=ColorSensorColor.none){
          countedColors[detectedColor.toInt()-1]++;//add to the count of the number of times the sensor has seen each color
          if(countedColors[0]>=7 && countedColors[1]>=7 && countedColors[2]>=7 && countedColors[3]>=7){//once we've seen each color 7 times (so 3.5 rotations), stop it from rotating.
            System.out.println("STOP. ROTATION IS DONE");
            System.out.println("STOP. ROTATION IS DONE");
            System.out.println("STOP. ROTATION IS DONE");
            System.out.println("STOP. ROTATION IS DONE");
            System.out.println("STOP. ROTATION IS DONE");
            subsystem.getSpinnerMotor().set(ControlMode.PercentOutput,0.0);
            doingRotation = false;
          }
        }
      }


    }
    else if(doingPosition){
      subsystem.getSpinnerMotor().set(ControlMode.PercentOutput,1.0);
      if(detectedColor!=lastDetectedColor){//the color has changed
        lastDetectedColor = detectedColor;
        System.out.println(lastDetectedColor);
        if(detectedColor==colorForPosition){//we have arrived at the correct color, so stop rotating
          System.out.println("STOP. POSITION IS DONE");
          System.out.println("STOP. POSITION IS DONE");
          System.out.println("STOP. POSITION IS DONE");
          System.out.println("STOP. POSITION IS DONE");
          System.out.println("STOP. POSITION IS DONE");
          colorForPosition = ColorSensorColor.none;
          subsystem.getSpinnerMotor().set(ControlMode.PercentOutput,0.0);
          doingPosition = false;
        }
      }
    }

  }



  

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}