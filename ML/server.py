from fastapi import FastAPI, Depends, Request
from pydantic import create_model
from pydantic import BaseModel
from ServerModelLaunch import predict

app = FastAPI()


class RecognitionRequest(BaseModel):
    image: list[int] = []
    mode: int


@app.post('/items/')
def main(request: RecognitionRequest) -> str:
    return predict(request.image)
