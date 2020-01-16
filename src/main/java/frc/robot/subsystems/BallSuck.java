/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.BallIntake;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

//Subsystem for climbing system
public class BallSuck extends SubsystemBase {

  
  // Motor to intake balls into the chassie
  private TalonSRX Intake;

  public BallSuck(int intake, int handle) {

    Intake = new TalonSRX(intake);
  }



  
  // @Override
  // public void initDefaultCommand(){
  //   setDefaultCommand(new ExampleCommand());
  // }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}