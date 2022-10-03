# Deployment Automation

In order to make the production code can be built and deployed on Heroku,
we have to follow the rules dictated by the service provider (i.e. Heroku).

Create a new file called `Procfile` in the root project directory.
It defines one or more processes that can run on Heroku's environment.
For our purpose today, you only need to define a single process called `web` that runs the application.
The content can be seen in the following snippet:

```procfile
web: java -Dserver.port=$PORT $JAVA_OPTS -jar target/*.jar
```

You also need to create a new file named `system.properties` in the root project directory.
It will be used to bind a specific version of Java to the deployment environment.
The content can be seen in the following snippet:

```properties
java.runtime.version=17
```

Add new files into local Git history and push the commit to GitHub:

```shell
git add Procfile system.properties
git commit
git push origin main
```

## Setting Up GitHub Actions

Create a new file with name `dpl.yml` in `.github/workflows` folder in your root project directory.
This file is used to execute _deployment_ by _runner_ from GitHub Actions.
The content of the `dpl.yml` is as follows:

```yml
---
name: Deploy

on:
  push:
    branches:
      - main

jobs:
  deploy:
    runs-on: ubuntu-22.04
    env:
      HEROKU_API_KEY: ${{ secrets.HEROKU_API_KEY }}
      HEROKU_APP_NAME: ${{ secrets.HEROKU_APP_NAME }}
    steps:
      - name: Checkout repository
        uses: actions/checkout@v3
      - name: Set up Ruby
        uses: ruby/setup-ruby@v1
        with:
          ruby-version: "2.7"
      - name: Install dpl
        run: gem install dpl
      - name: Deploy to Heroku
        run: dpl --provider=heroku --app=$HEROKU_APP_NAME --api-key=$HEROKU_API_KEY
      - uses: chrnorm/deployment-action@releases/v1
        name: Create GitHub deployment
        with:
          initial_status: success
          token: ${{ github.token }}
          target_url: https://${{ secrets.HEROKU_APP_NAME }}.herokuapp.com
          environment: production
```

After you perform all the procedures above, your new Spring Boot application is ready to deploy in Heroku.
If you open the GitHub Actions tab in your repository, it seems that a _workflow_ is already running,
but the status is failed because there is an error that says there are some parameters in _deployment_ job that were not found.
This thing happened because you have not configured the parameters needed by the _workflow_.
Now, you will configure those parameters:

1. Create a Heroku account if you have not done so.
   You can create a Heroku account on its login page on [this link](https://id.heroku.com/login).
   Once you have logged in to the dashboard page, create a Heroku app and take note of the name.
2. Copy API Key from your account. API Key can be found in `Account Settings -> API Key`. Keep your API Key and the information about your Heroku application in a text file with given format:
   ```
   HEROKU_API_KEY: <YOUR_API_KEY_VALUE>
   HEROKU_APP_NAME: <YOUR_HEROKU_APPLICATION_NAME>
   ```
3. Open your GitHub repository configuration and open the Secrets section for GitHub Actions (`Settings -> Secrets -> Actions`).
4. Add a new `repository secret` variable to do the _deployment_. Key-Value pair from the variable that you’ll make can be obtained from the information that you noted in the previous text file. Example can be seen below:
   ```
   (NAME)HEROKU_APP_NAME
   (VALUE)MY-APPLICATION
   ```
5. Keep the notes above somewhere outside the repository. **Do not commit your note file into Git, since it contains your Heroku key!**
6. Open GitHub Actions and re-run the failed _workflow_.

After you re-run _workflow_ and _deployment_ status become success (can be seen from the green checklist symbol on your repository),
you can access the application in `https://<heroku-application-name>.herokuapp.com`.
