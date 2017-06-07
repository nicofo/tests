from locust import HttpLocust, TaskSet, task
import json

class MyTaskSet(TaskSet):
  @task
  def my_task(self):
    payload = { "page": "2", "pageSize": "15","userId":"ag5zfmNydW1iaXQtMTMwNHIRCxIEVXNlchiAgICAy6eNCgw"}
    headers = {'content-type': 'application/json', 'accessToken':'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZzV6Zm1OeWRXMWlhWFF0TVRNd05ISVJDeElFVlhObGNoaUFnSUNBX2V5QkNndyIsImV4cCI6MTUwMTkwMTgxN30.x9RlaPOoaUfJ664kdqnZ5BXmYSQIpIWFqeHXp3enC2SxQZA3Bd-7Hx2g0IR8m6gk-U71CnvYgMdx909wZ70O7A'}
    r = self.client.post("/getLastCrumbs",json.dumps(payload), headers=headers, catch_response=True)
    print ("feta")


class MyLocust(HttpLocust):
  host = "https://crumbit-1304.appspot.com/_ah/api/crumbAPI/v1"
  min_wait = 1000
  max_wait = 1000
  task_set = MyTaskSet
