package hu.daniel.hari.exercises.cleanarchitecture.payrollcasestudy.app.usecase.usecases.find;

import hu.daniel.hari.exercises.cleanarchitecture.payrollcasestudy.app.entity.Employee;
import hu.daniel.hari.exercises.cleanarchitecture.payrollcasestudy.app.entity.paymentclassification.CommissionedPaymentClassification;
import hu.daniel.hari.exercises.cleanarchitecture.payrollcasestudy.app.entity.paymentclassification.HourlyPaymentClassification;
import hu.daniel.hari.exercises.cleanarchitecture.payrollcasestudy.app.entity.paymentclassification.PaymentClassification;
import hu.daniel.hari.exercises.cleanarchitecture.payrollcasestudy.app.entity.paymentclassification.SalariedPaymentClassification;
import hu.daniel.hari.exercises.cleanarchitecture.payrollcasestudy.app.usecase.HasResponse;
import hu.daniel.hari.exercises.cleanarchitecture.payrollcasestudy.app.usecase.TransactionalEmployeeGatewayUseCase;
import hu.daniel.hari.exercises.cleanarchitecture.payrollcasestudy.ports.primary.ui.requestresponse.request.GetEmployeeRequest;
import hu.daniel.hari.exercises.cleanarchitecture.payrollcasestudy.ports.primary.ui.requestresponse.response.EmployeeItem;
import hu.daniel.hari.exercises.cleanarchitecture.payrollcasestudy.ports.primary.ui.requestresponse.response.GetEmployeeResponse;
import hu.daniel.hari.exercises.cleanarchitecture.payrollcasestudy.ports.primary.ui.requestresponse.response.EmployeeItem.PaymentClassificationType;
import hu.daniel.hari.exercises.cleanarchitecture.payrollcasestudy.ports.secondary.database.EmployeeGateway;
import hu.daniel.hari.exercises.cleanarchitecture.payrollcasestudy.ports.secondary.database.TransactionalRunner;

public class GetEmployeeUseCase extends TransactionalEmployeeGatewayUseCase<GetEmployeeRequest> implements HasResponse<GetEmployeeResponse> {

	private EmployeeItemResponseCreator employeeItemResponseCreator = new EmployeeItemResponseCreator();
	private GetEmployeeResponse response;
	
	public GetEmployeeUseCase(TransactionalRunner transactionalRunner, EmployeeGateway employeeGateway) {
		super(transactionalRunner, employeeGateway);
	}

	@Override
	protected void executeInTransaction(GetEmployeeRequest request) {
		response = toResponse(employeeGateway.findById(request.employeeId));
	}

	private GetEmployeeResponse toResponse(Employee employee) {
		return new GetEmployeeResponse(employeeItemResponseCreator.toEmployeeItem(employee));
	}

	@Override
	public GetEmployeeResponse getResponse() {
		return response;
	}
	
	public static interface GetEmployeeUseCaseFactory {
		GetEmployeeUseCase getEmployeeUseCase();
	}
	
	private static class EmployeeItemResponseCreator {
		public EmployeeItem toEmployeeItem(Employee employee) {
			EmployeeItem employeeItem = new EmployeeItem();
			employeeItem.id = employee.getId();
			employeeItem.name = employee.getName();
			employeeItem.address = employee.getAddress();
			employeeItem.paymentClassificationType = toPaymentClassificationType(employee.getPaymentClassification());
			return employeeItem;
		}
		
		//TODO: duplicated
		private PaymentClassificationType toPaymentClassificationType(PaymentClassification paymentClassification) {
			if (paymentClassification instanceof SalariedPaymentClassification)
				return PaymentClassificationType.SALARIED;
			else if (paymentClassification instanceof HourlyPaymentClassification)
				return PaymentClassificationType.HOURLY;
			else if (paymentClassification instanceof CommissionedPaymentClassification)
				return PaymentClassificationType.COMMISSIONED;
			else
				throw new IllegalArgumentException();
		}
	}

}
