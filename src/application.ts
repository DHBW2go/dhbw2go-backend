import express from 'express'
import morgan from 'morgan'
import AuthenticationRoute from '@routes/authentication.route'

export default class Application {
    public environment: string
    public port: string | number

    constructor() {
        this.environment = process.env.ENVIRONMENT || 'development'
        this.port = process.env.PORT || 8000

        this.connectToDatabase()

        this.initializeMiddlewares()
        this.initializeErrorHandling()
        this.initializeRoutes()
        this.initializeSwagger()
    }

    public listen() {
        express().listen(this.port, () => {
            console.log(`=================================`)
            console.log(`======= Environment: ${this.environment} =======`)
            console.log(`======= Port: ${this.port} =======`)
            console.log(`=================================`)
        })
    }

    private async connectToDatabase() {
        try {
            //TODO: Connect to database
            console.log('Successfully connected to database.')
        } catch (error) {
            console.error('Error while connecting to database:', error)
        }
    }

    private async disconnectFromDatabase() {
        try {
            //TODO: Disconnect from database
            console.log('Successfully disconnected from database.')
        } catch (error) {
            console.error('Error while disconnecting to database:', error)
        }
    }

    private initializeMiddlewares() {
        express().use(morgan('tiny'))
        express().use(express.json())
        express().use(express.urlencoded({ extended: true }))
    }

    private initializeErrorHandling() {
        //TODO: Initialize error handling
    }

    private initializeRoutes() {
        new AuthenticationRoute()
    }

    private initializeSwagger() {}
}
