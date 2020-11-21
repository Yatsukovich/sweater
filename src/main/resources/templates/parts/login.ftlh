<#macro login path isRegisterForm>
    <head>
        <script>
        function onSubmit(token) {
            document.getElementById("registrForm").submit();
        }
    </script>
    </head>
    <form action="${path}" method="post" id="registrForm">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> User Name : </label>
            <div class="col-sm-6">
                <input type="text" name="username" value="<#if user??>${user.username}</#if>"
                       class="form-control ${(usernameError??)?string('is-invalid','')}"
                       placeholder="User name"/>
                <#if usernameError??>
                    <div class="invalid-feedback">
                        ${usernameError}
                    </div>
                </#if>
            </div>
        </div>

        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> Password:</label>
            <div class="col-sm-6">
                <input type="password" name="password"
                       class="form-control ${(passwordError??)?string('is-invalid','')}" placeholder="Password"/>
                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </div>

        </div>
        <#if isRegisterForm>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label"> Password:</label>
                <div class="col-sm-6">
                    <input type="password" name="password2"
                           class="form-control ${(password2Error??)?string('is-invalid','')}"
                           placeholder="Repeat password"/>
                    <#if password2Error??>
                        <div class="invalid-feedback">
                            ${password2Error}
                        </div>
                    </#if>
                </div>

            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label"> Email:</label>
                <div class="col-sm-6">
                    <input type="email" name="email" value="<#if user??>${user.email}</#if>"
                           class="form-control ${(emailError??)?string('is-invalid','')}"
                           placeholder="yourmail@some.com"/>
                    <#if emailError??>
                        <div class="invalid-feedback">
                            ${emailError}
                        </div>
                    </#if>
                </div>
            </div>
        </#if>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <#if !isRegisterForm><a href="/registration">Add new user</a></#if>
        <#if isRegisterForm>
        <button class="btn btn-primary g-recaptcha"
                type="submit"
                data-sitekey="6LdvuecZAAAAAN7wl2UdMPm96W0hxlSAKZXsOfsj"
                data-callback='onSubmit'
                data-action='submit'> Create</button>
        <#else> <button class="btn btn-primary" type="submit">Sign In</button>
        </#if>
    </form>
</#macro>
<#macro logout >
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button class="btn btn-primary" type="submit" value="Sign Out">Sign Out</button>
    </form>
</#macro>