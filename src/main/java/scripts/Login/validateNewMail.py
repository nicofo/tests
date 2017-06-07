from locust import HttpLocust, TaskSet, task
import json

class MyTaskSet(TaskSet):
  @task
  def my_task(self):
    payload = {"mail": "miquelferriol@gmail.com"}
    headers = {'content-type': 'application/json'}
    r = self.client.post("/validateNewMail",json.dumps(payload), headers=headers, catch_response=True)
    print r.text


class MyLocust(HttpLocust):
  host = "https://crumbit-1304.appspot.com/_ah/api/loginAPI/v1"
  min_wait = 1000
  max_wait = 1000
  task_set = MyTaskSet