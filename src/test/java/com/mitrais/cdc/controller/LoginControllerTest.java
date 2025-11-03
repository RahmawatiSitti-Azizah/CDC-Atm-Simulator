package com.mitrais.cdc.controller;

import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.service.LoginService;
import com.mitrais.cdc.util.ErrorConstant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class LoginControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private LoginController controllerInTest;

    @Mock
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controllerInTest).build();
    }

    @Test
    public void testHome_thenReturnHomeLoginPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("")).andExpect(MockMvcResultMatchers.view().name("Login"));
    }

    @Test
    public void testLogin_whenAccountPinCorrect_thenReturnTransactionMenuPage() throws Exception {
        Mockito.when(loginService.isAuthenticated(Mockito.any())).thenReturn(false);
        Mockito.when(loginService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(Mockito.mock(AccountDto.class));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("account", "112233")
                        .param("pin", "112233"))
                .andExpect(MockMvcResultMatchers.view().name("TransactionMenu"))
                .andReturn();

        Assertions.assertTrue(result.getRequest().getSession().getAttribute("account") != null);
        Mockito.verify(loginService, Mockito.times(1)).isAuthenticated(Mockito.any());
        Mockito.verify(loginService, Mockito.times(1)).login(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testLogin_whenAlreadyHaveSession_thenReturnTransactionMenuPage() throws Exception {
        Mockito.when(loginService.isAuthenticated(Mockito.any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/login").
                        content(("{\"account\": \"112233\"," +
                                "\"pin\":\"112233\"}").getBytes()))
                .andExpect(MockMvcResultMatchers.view().name("TransactionMenu"));

        Mockito.verify(loginService, Mockito.times(1)).isAuthenticated(Mockito.any());
        Mockito.verify(loginService, Mockito.never()).login(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testLogin_whenAccountPinIncorrect_thenReturnLoginPageWithErrorMessageInModel() throws Exception {
        Mockito.when(loginService.isAuthenticated(Mockito.any())).thenReturn(false);
        Mockito.when(loginService.login(Mockito.anyString(), Mockito.anyString())).thenThrow(new Exception(ErrorConstant.INVALID_ACCOUNT_PASSWORD));

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("account", "112233")
                        .param("pin", "112233"))
                .andExpect(MockMvcResultMatchers.view().name("Login"))
                .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", ErrorConstant.INVALID_ACCOUNT_PASSWORD));

        Mockito.verify(loginService, Mockito.times(1)).isAuthenticated(Mockito.any());
        Mockito.verify(loginService, Mockito.times(1)).login(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testLogout_returnLoginPageAndRemoveSessionAttribute() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/logout"))
                .andExpect(MockMvcResultMatchers.view().name("Login"))
                .andReturn();

        Assertions.assertTrue(result.getRequest().getSession().getAttribute("account") == null);
    }
}