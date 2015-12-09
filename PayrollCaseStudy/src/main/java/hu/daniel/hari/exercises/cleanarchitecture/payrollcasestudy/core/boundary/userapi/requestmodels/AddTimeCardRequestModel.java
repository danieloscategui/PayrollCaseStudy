package hu.daniel.hari.exercises.cleanarchitecture.payrollcasestudy.core.boundary.userapi.requestmodels;

import java.time.LocalDate;

public class AddTimeCardRequestModel {
	public final int employeeId;
	public final LocalDate date;
	public final int workingHoursQty;

	public AddTimeCardRequestModel(int employeeId, LocalDate date, int workingHoursQty) {
		this.employeeId = employeeId;
		this.date = date;
		this.workingHoursQty = workingHoursQty;
	}
}
