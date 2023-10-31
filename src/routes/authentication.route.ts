import express from 'express'
import AuthenticationController from '@controllers/authentication.controller'
import { LoginUser, RegisterUser } from '@/schemas/authentication.schema'

export default class AuthenticationRoute {
    public authenticationController = new AuthenticationController()

    constructor() {
        this.initializeRoutes()
    }

    private initializeRoutes() {
        express
            .Router()
            .post(
                '/authentication/register',
                async (request, response, next) => {
                    try {
                        const registerUser: RegisterUser = request.body
                        response.send(
                            await this.authenticationController.register(
                                registerUser
                            )
                        )
                    } catch (error) {
                        next(error)
                    }
                }
            )
        express
            .Router()
            .post('/authentication/login', async (request, response, next) => {
                try {
                    const loginUser: LoginUser = request.body
                    response.send(
                        await this.authenticationController.login(loginUser)
                    )
                } catch (error) {
                    next(error)
                }
            })
        express
            .Router()
            .delete(
                '/authentication/logout',
                async (request, response, next) => {
                    try {
                        const authorization: string | undefined =
                            request.header('authorization')
                        if (authorization !== undefined) {
                            response.send(
                                await this.authenticationController.logout(
                                    authorization
                                )
                            )
                        }
                        throw new Error() //add error message
                    } catch (error) {
                        next(error)
                    }
                }
            )
    }
}
