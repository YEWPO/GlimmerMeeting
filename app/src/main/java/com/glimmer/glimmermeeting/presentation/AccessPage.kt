package com.glimmer.glimmermeeting.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glimmer.glimmermeeting.R
import com.glimmer.glimmermeeting.ui.theme.GlimmerMeetingTheme
import com.glimmer.glimmermeeting.ui.theme.PinkLight

@Composable
fun AccessPage(
    onPageStateChanged: (String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.access_background),
                contentScale = ContentScale.FillWidth,
                alpha = 0.5f,
                alignment =
                Alignment.TopCenter
            )
    ) {
        AccessPageTitle()
        AccessPageContent(
            username = username,
            password = password,
            onUsernameChanged = { username = it },
            onPasswordChanged = { password = it },
            onPageStateChanged = onPageStateChanged
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AccessPagePreview() {
    GlimmerMeetingTheme {
        AccessPage(
            onPageStateChanged = {}
        )
    }
}

@Composable
fun AccessPageTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "登录",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 30.dp, top = 30.dp)
        )
    }
}

@Composable
fun AccessPageContent(
    username: String,
    password: String,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onPageStateChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        AccessPageInputField(
            username = username,
            password = password,
            onUsernameChanged = onUsernameChanged,
            onPasswordChanged = onPasswordChanged
        )
        AccessPageActionField(
            onPageStateChanged = onPageStateChanged
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessPageInputField(
    username: String,
    password: String,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, top = 30.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White
            ),
            value = username,
            onValueChange = { onUsernameChanged(it) },
            placeholder = { Text(text = "请输入账户") },
            label = { Text(text = "账户") },
            leadingIcon = { Icon(Icons.Outlined.AccountCircle, contentDescription = "account_circle") }
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, top = 25.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White
            ),
            value = password,
            onValueChange = { onPasswordChanged(it) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            placeholder = { Text(text = "请输入密码") },
            label = { Text(text = "密码") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = { Icon(painter = painterResource(id = R.drawable.outline_key), contentDescription = "key") },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(id =
                        if (passwordVisible)
                            R.drawable.outline_visibility
                        else
                            R.drawable.outline_visibility_off
                        ),
                        contentDescription = "password_visible"
                    )
                }
            }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        TextButton(
            onClick = {  },
            modifier = Modifier
                .padding(end = 30.dp)
        ) {
            Text(
                text = "忘记密码?",
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Composable
fun AccessPageActionField(
    onPageStateChanged: (String) -> Unit
) {
    var rulesAgree by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(start = 40.dp, end = 40.dp),
            onClick = { onPageStateChanged("MainPage") },
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "登录",
                fontSize = 17.sp
            )
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(start = 40.dp, end = 40.dp),
            onClick = { },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PinkLight
            )
        ) {
            Text(
                text = "新用户激活",
                fontSize = 17.sp,
                color = Color.Black
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = rulesAgree,
                onClick = { rulesAgree = !rulesAgree }
            )
            Text(
                text = "同意《用户使用条例》和《隐私政策》",
                fontSize = 14.sp
            )
        }
    }
}