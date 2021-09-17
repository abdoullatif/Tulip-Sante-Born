package com.example.tulipsante.viewModel;

import android.app.Application;

import androidx.test.platform.app.InstrumentationRegistry;

import junit.framework.TestCase;

import org.junit.Test;

public class LoginViewModelTest extends TestCase {

    LoginViewModel loginViewModel;

    @Override
    public void setUp() throws Exception {
        loginViewModel = new LoginViewModel((Application) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext());
    }

    @Test
    public void testCheckValidLogin() {
        boolean expected = loginViewModel.checkValidLogin("alpha","test123");
        assertTrue(expected);
    }

    @Test
    public void testGetMedecinId() {
        String idMedecin = loginViewModel.getIdMedecin("alha");
        assertEquals(idMedecin,"1001");
    }

    @Test
    public void testCheckTagEqualsUid() {
        boolean expected = loginViewModel.checkTagEqualsUid("bbc46732");
        assertTrue(expected);
    }
}