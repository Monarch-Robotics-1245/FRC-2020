/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;


import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.robot.Constants;
import frc.robot.commands.DriveTank;
import frc.robot.subsystems.Drivetrain;


import frc.robot.OI;


import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
//import edu.wpi.first.wpilibj.Timer;


/**
 * The auto command to drive forward
 */
public class DriveAuto extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final Drivetrain subsystem;
//    private final Timer timer;




    /**
     * @param subsystem The subsystem used by this command.
     */
    public DriveAuto(Drivetrain subsystem) {
        this.subsystem = subsystem;
//        timer = new Timer();
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(subsystem);

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        subsystem.ldrive(0.75);
        subsystem.rdrive(0.75);
    }



    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        subsystem.ldrive(0.0);
        subsystem.rdrive(0.0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}