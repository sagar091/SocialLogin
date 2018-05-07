# SocialLogin

Facebook
App Id : 327031817795722

Facebook      
keytool -exportcert -alias androiddebugkey -keystore "C:\Users\Sagar\\.android\debug.keystore" | "E:\Software\openssl-1.0.2j-fips-x86_64\OpenSSL\bin\openssl.exe" sha1 -binary | "E:\Software\openssl-1.0.2j-fips-x86_64\OpenSSL\bin\openssl.exe" base64

Key Hash : MAg9IEFG2QitM24lmZwZ6+mWzqI=


============================================

Google

keytool -exportcert -keystore "C:\Users\Sagar\\.android\debug.keystore" -list -v

SHA1 : 30:08:3D:20:41:46:D9:08:AD:33:6E:25:99:9C:19:EB:E9:96:CE:A2


	GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());

	if (acct != null) {

		String personName = acct.getDisplayName();
	
		String personGivenName = acct.getGivenName();
	
		String personFamilyName = acct.getFamilyName();
	
		String personEmail = acct.getEmail();
	
		String personId = acct.getId();
	
		Uri personPhoto = acct.getPhotoUrl();
	
	}
