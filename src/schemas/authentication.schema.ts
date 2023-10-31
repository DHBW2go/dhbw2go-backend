import { IsEmail, Length } from 'class-validator'

export class RegisterUser {
    @IsEmail()
    email!: string

    @Length(8, 64)
    password!: string

    @Length(3, 32)
    displayName!: string
}

export class LoginUser {
    @IsEmail()
    email!: string

    @Length(8, 64)
    password!: string
}
