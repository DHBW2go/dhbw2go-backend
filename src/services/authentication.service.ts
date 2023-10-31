import { LoginUser, RegisterUser } from '@/schemas/authentication.schema'
import { User } from '@models/user.model'

export default class AuthenticationService {
    public async register(registerUser: RegisterUser): Promise<User> {
        return {
            _id: '1',
            email: registerUser.email,
            password: registerUser.password,
            displayName: registerUser.displayName,
        }
    }

    public async login(loginUser: LoginUser): Promise<User> {
        return {
            _id: '1',
            email: loginUser.email,
            password: loginUser.password,
            displayName: 'Test',
        }
    }

    public async logout() {}
}
