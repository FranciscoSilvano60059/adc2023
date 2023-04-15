package pt.unl.fct.di.apdc.firstwebapp.util;


public class ChangePasswordData {
        public String oldPassword;
        public String newPassword;
        public String newPasswordConfirmation;
		public String username;
		
		public ChangePasswordData() {
			}
        public ChangePasswordData(String username, String oldPassword, String newPassword, String newPasswordConfirmation) {
        	this.username = username;
        	this.oldPassword=oldPassword;
    		this.newPassword = newPassword;
    		this.newPasswordConfirmation = newPasswordConfirmation;
        }

        public boolean validChangePassword() {
            return oldPassword != null && newPassword != null && newPasswordConfirmation != null && newPassword.equals(newPasswordConfirmation);
        }
    }
