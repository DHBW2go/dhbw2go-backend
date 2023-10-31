import { Body, Delete, Header, Post, Route } from 'tsoa'
import { LoginUser, RegisterUser } from '@/schemas/authentication.schema'
import { Controller } from '@tsoa/runtime'
import { User } from '@models/user.model'
import AuthenticationService from '@services/authentication.service'

@Route('authentication')
export default class AuthenticationController extends Controller {
    public authenticationService = new AuthenticationService()

    @Post('/register')
    public async register(@Body() registerUser: RegisterUser): Promise<User> {
        const user: User =
            await this.authenticationService.register(registerUser)
        return user
    }

    @Post('/login')
    public async login(@Body() loginUser: LoginUser): Promise<User> {
        const user: User = await this.authenticationService.login(loginUser)
        return user
    }

    @Delete('/logout')
    public async logout(@Header('Authorization') authorization: string) {
        authorization.replace('Bearer ', '')
    }
}
