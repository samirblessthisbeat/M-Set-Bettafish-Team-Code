package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.Drive;
import org.firstinspires.ftc.teamcode.Robot;

import static java.lang.Math.abs;

/**
 * Created by tejbade on 10/8/18.
 */

@TeleOp(name="teleop")
public class TeleOp_ extends LinearOpMode {
	Robot r = new Robot();
	Drive dr;

	public void runOpMode() throws InterruptedException {
		// Initialize the drive system variables.
		// The init() method of the hardware class does all the work here
		r.init(hardwareMap, telemetry);
		dr = new Drive(r);
		telemetry.update();
		double speed = 1;
		waitForStart();

		while (opModeIsActive()) {
			// Joystick value test
			telemetry.addData("x", gamepad1.left_stick_x + "");
			telemetry.addData("y", gamepad1.left_stick_y + "");
			telemetry.update();

			if(gamepad1.dpad_up)
				forward();
			else if(gamepad1.dpad_down)
				backward();
			else if(gamepad1.right_bumper)
				turnRight();
			else if(gamepad1.left_bumper)
				turnLeft();
			else if(gamepad1.right_bumper)
				turnRight();
			else if(gamepad1.left_bumper)
				turnLeft();
			if(gamepad2.y)
				r.winch.setPower(0.4);
			else
				r.winch.setPower(0);
			if(gamepad2.a)
				r.winch.setPower(-0.4);
			else
				r.winch.setPower(0);
			idle();
		}
		rest();
	}

	public void forward() {
		r.rightBack.setPower(-1);
		r.rightFront.setPower(1);
		r.leftBack.setPower(1);
		r.leftFront.setPower(1);
	}
	public void backward() {
		r.rightBack.setPower(1);
		r.rightFront.setPower(-1);
		r.leftBack.setPower(-1);
		r.leftFront.setPower(-1);
	}
	public void turnRight() {
		r.rightBack.setPower(-1);
		r.rightFront.setPower(1);
		r.leftBack.setPower(-1);
		r.leftFront.setPower(-1);
	}
	public void turnLeft() {
		r.rightBack.setPower(1);
		r.rightFront.setPower(-1);
		r.leftBack.setPower(1);
		r.leftFront.setPower(1);
	}
	public void MecanumStrafeLeft(double power, int time) {
		// wheels move in opposite directions so that forward/backward forces cancel out
		r.leftFront.setPower(power);
		r.leftBack.setPower(-power);
		r.rightFront.setPower(-power);
		r.rightBack.setPower(power);
		sleep(time);
		rest();
	}
	public void MecanumStrafeRight(double power, int time) {
		// wheels move in opposite directions so that forward/backward forces cancel out
		r.leftFront.setPower(-power);
		r.leftBack.setPower(power);
		r.rightFront.setPower(power);
		r.rightBack.setPower(-power);
		sleep(time);
		rest();
	}
	public void rest() {
		r.rightBack.setPower(0);
		r.rightFront.setPower(0);
		r.leftBack.setPower(0);
		r.leftFront.setPower(0);
	}
	// OBSOLETE FROM HERE BELOW
	// pre-coded drive Speeds
	// Mecanum power is determined by Speed, Turn, and Strafe
	public void MecanumDrive() {
		double Speed = -gamepad1.left_stick_y;
		double Turn = gamepad1.left_stick_x;
		double Strafe = gamepad1.right_stick_x;
		double MAX_SPEED = 1.0;
		holonomic(Speed, Turn, Strafe, MAX_SPEED);
	}
// Front, Back, rightstrafe, leftstrafe
	public void holonomic(double Speed, double Turn, double Strafe, double MAX_SPEED) {
//EQ:      Left Front = +Speed + Turn - Strafe      Right Front = +Speed - Turn + Strafe
//EQ:      Left Rear  = +Speed + Turn + Strafe      Right Rear  = +Speed - Turn - Strafe

		double Magnitude = abs(Speed) + abs(Turn) + abs(Strafe);
		Magnitude = (Magnitude > 1) ? Magnitude : 1; //Set scaling to keep -1,+1 range

		r.leftFront.setPower(scaleInput(Speed) + scaleInput(Turn) - scaleInput(Strafe));
		if (r.leftBack != null) {
			r.leftBack.setPower(scaleInput(Speed) + scaleInput(Turn) + scaleInput(Strafe));
		}
		r.rightFront.setPower(scaleInput(Speed) - scaleInput(Turn) + scaleInput(Strafe));
		if (r.rightBack != null) {
			r.rightBack.setPower(scaleInput(Speed) - scaleInput(Turn) - scaleInput(Strafe));
		}
	}

	double scaleInput(double dVal)  {
		double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
				0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };
		// get the corresponding index for the scaleInput array.
		int index = (int) (dVal * 16.0);
		// index should be positive.
		if (index < 0) {
			index = -index;
		}
		// index cannot exceed size of array minus 1.
		if (index > scaleArray.length) {
			index = scaleArray.length;
		}
		// get value from the array.
		double dScale = 0.0;
		if (dVal < 0) {
			dScale = -scaleArray[index];
		} else {
			dScale = scaleArray[index];
		}
		// return scaled value.
		return dScale;
	}

}
